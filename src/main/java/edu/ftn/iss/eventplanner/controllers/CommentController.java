package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.ApproveCommentDTO;
import edu.ftn.iss.eventplanner.dtos.CommentResponseDTO;
import edu.ftn.iss.eventplanner.dtos.CreateCommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    // Endpoint za kreiranje komentara
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CreateCommentDTO createCommentDTO) {
        // Ovdje bi bila logika za kreiranje komentara, trenutno vraćamo stub odgovor
        CommentResponseDTO response = new CommentResponseDTO(
                1L, createCommentDTO.getContent(), createCommentDTO.getRating(), createCommentDTO.getDate(), "pending", createCommentDTO.getProductId());
        return ResponseEntity.status(201).body(response);
    }

    // Endpoint za odobravanje ili brisanje komentara
    @PutMapping("/approve")
    public ResponseEntity<CommentResponseDTO> approveOrDeleteComment(@Valid @RequestBody ApproveCommentDTO approveCommentDTO) {
        // Ovdje bi bila logika za odobravanje ili brisanje komentara, trenutno vraćamo stub odgovor
        CommentResponseDTO response = new CommentResponseDTO(approveCommentDTO.getCommentId(), "Approved", 5, LocalDate.now(), "approved", 1L);
        return ResponseEntity.ok(response);
    }

    // Endpoint za dohvat svih komentara
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        // Ovdje bi bila logika za dohvat svih komentara, trenutno vraćamo stub odgovor
        List<CommentResponseDTO> comments = List.of(
                new CommentResponseDTO(1L, "Great product!", 5, LocalDate.now(), "approved", 1L),
                new CommentResponseDTO(2L, "Not bad", 3, LocalDate.now(), "pending", 1L)
        );
        return ResponseEntity.ok(comments);
    }
}

