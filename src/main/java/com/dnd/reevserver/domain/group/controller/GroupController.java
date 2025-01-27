package com.dnd.reevserver.domain.group.controller;

import com.dnd.reevserver.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

}
