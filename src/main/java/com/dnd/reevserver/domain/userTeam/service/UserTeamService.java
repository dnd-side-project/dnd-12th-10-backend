package com.dnd.reevserver.domain.userTeam.service;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.repository.MemberRepository;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.domain.team.service.TeamService;
import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import com.dnd.reevserver.domain.userTeam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTeamService {
    private final UserTeamRepository userTeamRepository;
    private final MemberService memberService;
    private final TeamService teamService;

    @Transactional
    public String mapping(String userId, Long groupId){
        Member member = memberService.findById(userId);
        Team team = teamService.findById(groupId);
        UserTeam join = new UserTeam(member,team);
        userTeamRepository.save(join);
        member.addUserTeam(join);
        team.addUserTeam(join);
        return "굿";
    }
}
