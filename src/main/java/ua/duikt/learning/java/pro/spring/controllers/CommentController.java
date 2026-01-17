package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.AddCommentRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateCommentRequest;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.service.CommentService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/user/{userId}/issues/{issueId}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long issueId,
                                             @PathVariable Long userId,
                                             @RequestBody @Valid AddCommentRequest request) {
        commentService.addComment(issueId, request.getContent(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment added");
    }

    @GetMapping("/issues/{issueId}/comments")
    public ResponseEntity<List<IssueComment>> getComments(@PathVariable Long issueId) {
        return ResponseEntity.ok(commentService.getComments(issueId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequest request) {

        commentService.updateComment(commentId, request.getContent());
        return ResponseEntity.ok("Comment updated");
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
