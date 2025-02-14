package com.dnd.reevserver.domain.comment.controller;

import com.dnd.reevserver.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/commet")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
}
