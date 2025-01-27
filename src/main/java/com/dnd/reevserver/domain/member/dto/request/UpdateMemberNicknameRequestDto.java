package com.dnd.reevserver.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberNicknameRequestDto {
    private String userId;
    private String nickname;
}
