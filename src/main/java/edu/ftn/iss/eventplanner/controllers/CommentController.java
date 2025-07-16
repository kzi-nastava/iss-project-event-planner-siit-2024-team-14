package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.comments.ApproveCommentDTO;
import edu.ftn.iss.eventplanner.dtos.comments.CommentResponseDTO;
import edu.ftn.iss.eventplanner.dtos.comments.CreateCommentDTO;
import edu.ftn.iss.eventplanner.dtos.notifications.NotificationDTO;
import edu.ftn.iss.eventplanner.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CreateCommentDTO createCommentDTO) {
        CommentResponseDTO response = commentService.createComment(createCommentDTO);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/approve")
    public ResponseEntity<CommentResponseDTO> approveComment(@Valid @RequestBody ApproveCommentDTO approveCommentDTO) {
        CommentResponseDTO response = commentService.approveComment(approveCommentDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/delete")
    public ResponseEntity<CommentResponseDTO> deleteComment(@Valid @RequestBody ApproveCommentDTO approveCommentDTO) {
        CommentResponseDTO response = commentService.deleteComment(approveCommentDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        List<CommentResponseDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CommentResponseDTO>> getPendingComments() {
        List<CommentResponseDTO> comments = commentService.getPendingComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<CommentResponseDTO>> getApprovedComments() {
        List<CommentResponseDTO> comments = commentService.getApprovedComments();
        return ResponseEntity.ok(comments);
    }
}

