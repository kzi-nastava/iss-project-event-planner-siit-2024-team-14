package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.ApproveCommentDTO;
import edu.ftn.iss.eventplanner.dtos.CommentResponseDTO;
import edu.ftn.iss.eventplanner.dtos.CreateCommentDTO;
import edu.ftn.iss.eventplanner.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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

    // Endpoint za odobravanje ili logičko brisanje komentara (Admin)
    @PutMapping("/approve")
    public ResponseEntity<CommentResponseDTO> approveOrDeleteComment(@Valid @RequestBody ApproveCommentDTO approveCommentDTO) {
        CommentResponseDTO response = commentService.approveOrDeleteComment(approveCommentDTO);
        return ResponseEntity.ok(response);
    }

    // Endpoint za dohvat svih komentara (Admin)
    @GetMapping("/all")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        List<CommentResponseDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // Endpoint za dohvat komentara sa statusom 'PENDING' (Admin)
    @GetMapping("/pending")
    public ResponseEntity<List<CommentResponseDTO>> getPendingComments() {
        List<CommentResponseDTO> comments = commentService.getPendingComments();
        return ResponseEntity.ok(comments);
    }


    // Endpoint za dohvat odobrenih komentara (Korisnici)
    @GetMapping("/approved")
    public ResponseEntity<List<CommentResponseDTO>> getApprovedComments() {
        List<CommentResponseDTO> comments = commentService.getApprovedComments();
        return ResponseEntity.ok(comments);
    }

}

