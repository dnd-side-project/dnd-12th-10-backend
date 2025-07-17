package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.entity.RetrospectCategory;
import com.dnd.reevserver.domain.category.repository.CategoryRepository;
import com.dnd.reevserver.domain.category.repository.RetrospectCategoryRepository;
import com.dnd.reevserver.domain.category.repository.batch.RetrospectCategoryBatchRepository;
import com.dnd.reevserver.domain.like.repository.LikeRepository;
import com.dnd.reevserver.domain.retrospect.dto.request.BookmarkRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.response.*;
import com.dnd.reevserver.domain.retrospect.entity.Bookmark;
import com.dnd.reevserver.domain.retrospect.exception.*;
import com.dnd.reevserver.domain.retrospect.repository.BookmarkRepository;
import com.dnd.reevserver.domain.comment.repository.CommentRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.search.dto.response.SearchRetrospectResponseDto;
import com.dnd.reevserver.domain.statistics.service.StatisticsService;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.service.TeamService;
import com.dnd.reevserver.global.util.TimeStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.Tuple;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final MemberService memberService;
    private final TeamService teamService;
    private final TimeStringUtil timeStringUtil;
    private final CommentRepository commentRepository;
    private final StatisticsService statisticsService;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;
    private final CategoryRepository categoryRepository;
    private final RetrospectCategoryBatchRepository retrospectCategoryBatchRepository;
    private final RetrospectCategoryRepository retrospectCategoryRepository;

    //단일회고 조회
    @Transactional(readOnly = true)
    public RetrospectDetailResponseDto getRetrospectById(String userId, Long retrospectId) {
        if(userId.isEmpty()) {
            throw new MemberNotFoundException();
        }
        Tuple tuple = retrospectRepository.findByIdWithBookmarkAndCommentCount(retrospectId).orElseThrow(RetrospectNotFoundException::new);

        Retrospect retrospect = tuple.get(0, Retrospect.class); // 첫 번째 값: Retrospect 객체
        boolean isBookmarked = tuple.get(1, Boolean.class); // 두 번째 값: 북마크 여부
        long commentCount = tuple.get(2, Long.class); // 세 번째 값: 댓글 수
        List<String> rcList = retrospectRepository.findCategoryNamesByRetrospectId(retrospectId);

        return convertToDetailDto(retrospect, isBookmarked, commentCount, rcList, userId);
    }

    //회고 목록 조회
    @Transactional(readOnly = true)
    public List<RetrospectResponseDto> getAllRetrospectByGruopId(String userId, Long groupId) {
        if(userId.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if(groupId!=null) {
            List<Tuple> list = retrospectRepository.findAllByTeamId(groupId, userId);
            return list.stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class),
                        retrospectRepository.findCategoryNamesByRetrospectId(tuple.get(0, Retrospect.class).getRetrospectId())))
                .toList();
        }

        List<Tuple> list = retrospectRepository.findAllByUserId(userId);
        return list.stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class),
                        retrospectRepository.findCategoryNamesByRetrospectId(tuple.get(0, Retrospect.class).getRetrospectId())))
                .toList();
    }

    // 유저 별 회고 작성 현황 조회
    // action : all -> 모임, 개인 전체
    // action : group -> 모임만
    // action : personal -> 개인만
    @Transactional(readOnly = true)
    public RetrospectByMemberResponseDto getRetrospectByMemberAndGroupExisted(String userId, String action){
        List<RetrospectSimpleDto> resultList;
        if(userId.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if("all".equals(action)) resultList = retrospectRepository.findSimpleByUserId(userId);
        else if("group".equals(action)) resultList = retrospectRepository.findSimpleByUserIdAndGroupExisted(userId);
        else if("personal".equals(action)) resultList = retrospectRepository.findSimpleByUserIdAndGroupNotExisted(userId);
        else throw new WrongActionException();

        return RetrospectByMemberResponseDto.builder()
                .count(resultList.size())
                .retrospectList(
                        resultList.stream()
                                .map(r -> RetrospectByMemberItemResponseDto.builder()
                                        .retrospectId(r.getRetrospectId())
                                        .title(r.getTitle())
                                        .content(r.getContent())
                                        .type(r.getGroupId() == null ? "개인" : "모임")
                                        .build()
                                )
                                .toList()
                )
                .build();
    }

    //회고 작성
    @Transactional
    public AddRetrospectResponseDto addRetrospect(String userId, AddRetrospectRequestDto requestDto) {
        Member member = memberService.findById(userId);
        Retrospect retrospect;
        if(requestDto.groupId()!=null) {
            Team team = teamService.findById(requestDto.groupId());
            retrospect = Retrospect.builder()
                .member(member)
                .team(team)
                .title(requestDto.title())
                .content(requestDto.content())
                .build();
        }
        else{
            retrospect = Retrospect.builder()
                    .member(member)
                    .title(requestDto.title())
                    .content(requestDto.content())
                    .build();
        }
        retrospectRepository.save(retrospect);
        statisticsService.writeUserRepoStatistics(userId);

        List<Category> categories = categoryRepository.findByCategoryNameIn(requestDto.categoryNames());

        List<RetrospectCategory> rcList = categories.stream()
                .map(category -> new RetrospectCategory(retrospect, category))
                .toList();
        retrospectCategoryBatchRepository.saveAll(rcList);

        LocalDateTime now = LocalDateTime.now();

        statisticsService.writeRetrospectCategoryStatistics(userId, now.getYear(), now.getMonthValue(),
                categories.stream().map(Category::getCategoryName).toList());

        return new AddRetrospectResponseDto(retrospect.getRetrospectId());
    }

    public Retrospect findById(Long retrospectId) {
        return retrospectRepository.findById(retrospectId).orElseThrow(RetrospectNotFoundException::new);
    }

    @Transactional
    public RetrospectSingleResponseDto updateRetrospect(String userId, UpdateRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(userId)){
            throw new RetrospectAuthorException();
        }
        retrospect.updateRetrospect(requestDto.title(), requestDto.content());

        List<String> oldCategoryNames = retrospectRepository.findCategoryNamesByRetrospectId(requestDto.retrospectId());

        retrospectCategoryRepository.deleteAllByRetrospect(retrospect);

        List<Category> newCategoryNames = categoryRepository.findByCategoryNameIn(requestDto.categoryNames());

        List<RetrospectCategory> rcList = newCategoryNames.stream()
                .map(category -> new RetrospectCategory(retrospect, category))
                .toList();
        retrospectCategoryBatchRepository.saveAll(rcList);

        statisticsService.updateRetrospectCategoryStatistics(userId, retrospect.getCreatedAt(), oldCategoryNames
                , newCategoryNames.stream().map(Category::getCategoryName).toList());

        return convertToSingleDto(retrospect, requestDto.categoryNames());
    }

    @Transactional
    public DeleteRetrospectResponseDto deleteRetrospect(String userId, DeleteRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(userId)){
            throw new RetrospectAuthorException();
        }
        long retrospectId = retrospect.getRetrospectId();
        commentRepository.deleteAllByRetrospectRetrospectId(retrospectId);
        retrospectCategoryRepository.deleteAllByRetrospect(retrospect);
        retrospectRepository.delete(retrospect);
        return new DeleteRetrospectResponseDto(retrospectId);
    }

    //회고수 계산
    @Transactional(readOnly = true)
    public long countByGroupId(Long groupId) {
        return retrospectRepository.countByGroupId(groupId);
    }

    //통합검색에서 검색
    @Transactional(readOnly = true)
    public List<SearchRetrospectResponseDto> searchForKeyword(String keyword){
        List<Retrospect> retrospects = retrospectRepository.searchForKeyword(keyword);
        return retrospects.stream()
            .map(this::convertToRetrospectResponse)
            .toList();
    }

    //통합검색후 상세
    @Transactional(readOnly = true)
    public Slice<SearchRetrospectResponseDto> searchForKeywordParti(String keyword, Pageable pageable){
         return retrospectRepository.searchForKeywordParti(keyword, pageable)
            .map(this::convertToRetrospectResponse);
    }


    public int getLikeCount(Long retrospectId) {
        return likeRepository.getLikeCount(retrospectId);
    }

    // 회원의 북마크된 전체 회고 조회
    public List<RetrospectResponseDto> getBookmarkedRetrospects(String userId){
        return retrospectRepository.findRetrospectsByUserIdWithBookmarked(userId).stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class),
                        retrospectRepository.findCategoryNamesByRetrospectId(tuple.get(0, Retrospect.class).getRetrospectId())))
                .toList();
    }

    // 회원의 북마크된 그룹 별 회고 조회
    public List<RetrospectResponseDto> getBookmarkedRetrospectsWithGroupId(String userId, Long groupId){
        return retrospectRepository.findRetrospectsByUserIdWithBookmarkedAndGroupId(userId, groupId).stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class),
                        retrospectRepository.findCategoryNamesByRetrospectId(tuple.get(0, Retrospect.class).getRetrospectId())))
                .toList();
    }

    // 북마크 기능 (insert)
    @Transactional
    public void insertBookmark(String userId, BookmarkRequestDto dto){
        if(bookmarkRepository.existsByRetrospectRetrospectIdAndMemberUserId(dto.retrospectId(), userId))
            throw new BookmarkAlreadyExistedException();
        bookmarkRepository.save(Bookmark.builder()
                .member(memberService.findById(userId))
                .retrospect(findById(dto.retrospectId()))
                .build());
    }

    // 북마크 취소 (delete)
    @Transactional
    public void deleteBookmark(String userId, BookmarkRequestDto dto){
        bookmarkRepository.deleteByRetrospectRetrospectIdAndMemberUserId(dto.retrospectId(), userId);
    }

    private RetrospectResponseDto convertToDto(Retrospect retrospect, boolean isBookmarked, long commentCnt, List<String> categoriesName) {
        return RetrospectResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(getLikeCount(retrospect.getRetrospectId()))
                .groupName(retrospect.getTeam() != null ? retrospect.getTeam().getGroupName() : null)
                .groupId(retrospect.getTeam() != null ? retrospect.getTeam().getGroupId() : null)
                .commentCount(commentCnt)
                .bookmark(isBookmarked)
                .categories(categoriesName)
                .build();
    }

    //작성자인지 여부 포함
    private RetrospectDetailResponseDto convertToDetailDto(Retrospect retrospect, boolean isBookmarked, long commentCnt, List<String> categoriesName, String userId) {
        return RetrospectDetailResponseDto.builder()
            .retrospectId(retrospect.getRetrospectId())
            .title(retrospect.getTitle())
            .content(retrospect.getContent())
            .userName(retrospect.getMember().getNickname())
            .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
            .likeCount(getLikeCount(retrospect.getRetrospectId()))
            .groupName(retrospect.getTeam() != null ? retrospect.getTeam().getGroupName() : null)
            .groupId(retrospect.getTeam() != null ? retrospect.getTeam().getGroupId() : null)
            .commentCount(commentCnt)
            .bookmark(isBookmarked)
            .categories(categoriesName)
            .isAuthor((retrospect.getMember().getUserId()).equals(userId))
            .build();
    }

    private RetrospectSingleResponseDto convertToSingleDto(Retrospect retrospect, List<String> categoriesName) {
        return RetrospectSingleResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(getLikeCount(retrospect.getRetrospectId()))
                .groupName(retrospect.getTeam() != null ? retrospect.getTeam().getGroupName() : null)
                .groupId(retrospect.getTeam() != null ? retrospect.getTeam().getGroupId() : null)
                .commentCount(commentRepository.countByRetrospect(retrospect))
                .categories(categoriesName)
                .build();
    }

    private SearchRetrospectResponseDto convertToRetrospectResponse(Retrospect retrospect){
        return SearchRetrospectResponseDto.builder()
            .retrospectId(retrospect.getRetrospectId())
            .title(retrospect.getTitle())
            .userName(retrospect.getMember().getNickname())
            .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
            .build();
    }
}
