package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.*;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final NotificationWebSocketService notificationWebSocketService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public CommentResponseDTO createComment(CreateCommentDTO dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setRating(dto.getRating());
        comment.setDate(LocalDate.now());
        comment.setStatus(Status.PENDING);

        if (dto.getProductId() != null) {
            comment.setProduct(findProductById(dto.getProductId()));
        }
        if (dto.getServiceId() != null) {
            comment.setService(findServiceById(dto.getServiceId()));
        }
        if (dto.getCommenterId() != null) {
            comment.setCommenter(findUserById(dto.getCommenterId()));
        }

        Comment savedComment = commentRepository.save(comment);
        CommenterInfo commenterInfo = getCommenterInfo(savedComment.getCommenter());

        return buildCommentResponse(savedComment, commenterInfo);
    }

    public CommentResponseDTO deleteComment(ApproveCommentDTO dto) {
        Comment comment = findCommentById(dto.getCommentId());
        comment.setStatus(Status.DELETED);
        commentRepository.save(comment);

        String message = "Your comment: '" + comment.getContent() + "' for '" + getEntityName(comment) + "' has been rejected!";
        sendNotification(comment.getCommenter().getId(), message, comment.getId());

        CommenterInfo commenterInfo = getCommenterInfo(comment.getCommenter());
        return buildCommentResponse(comment, commenterInfo);
    }

    public CommentResponseDTO approveComment(ApproveCommentDTO dto) {
        Comment comment = findCommentById(dto.getCommentId());
        comment.setStatus(Status.APPROVED);
        commentRepository.save(comment);

        String entityName = getEntityName(comment);
        String message = "Your comment: '" + comment.getContent() + "' for '" + entityName + "' has been approved!";
        sendNotification(comment.getCommenter().getId(), message, comment.getId());

        notifyProvider(comment, entityName);
        CommenterInfo commenterInfo = getCommenterInfo(comment.getCommenter());

        return buildCommentResponse(comment, commenterInfo);
    }

    public List<CommentResponseDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDTO> getPendingComments() {
        return commentRepository.findByStatus(Status.PENDING).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDTO> getApprovedComments() {
        return commentRepository.findByStatus(Status.APPROVED).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- POMOĆNE METODE ---

    private Comment findCommentById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    private Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private edu.ftn.iss.eventplanner.entities.Service findServiceById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void sendNotification(Integer userId, String message, Integer commentId) {
        NotificationDTO notification = notificationService.createNotification(userId, message, commentId, null);
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
        notificationWebSocketService.sendWebSocketNotification(userId, notification);
    }

    private void notifyProvider(Comment comment, String entityName) {
        User provider = (comment.getProduct() != null) ? comment.getProduct().getProvider()
                : (comment.getService() != null) ? comment.getService().getProvider()
                : null;

        if (provider != null) {
            String message = "User: " + comment.getCommenter().getEmail() + " commented on your " + entityName + ": '" + comment.getContent() + "'";
            sendNotification(provider.getId(), message, comment.getId());
        }
    }

    private String getEntityName(Comment comment) {
        if (comment.getProduct() != null) return comment.getProduct().getName();
        if (comment.getService() != null) return comment.getService().getName();
        return "N/A";
    }

    private CommenterInfo getCommenterInfo(User commenter) {
        if (commenter == null) {
            return new CommenterInfo("Anonymous", "", null);
        }

        if (commenter instanceof EventOrganizer organizer) {
            return new CommenterInfo(organizer.getName(), organizer.getSurname(), organizer.getProfilePhoto());
        } else if (commenter instanceof ServiceAndProductProvider provider) {
            return new CommenterInfo(provider.getCompanyName(), "", null);
        }

        return new CommenterInfo("Unknown", "", null);
    }


    private CommentResponseDTO mapToDTO(Comment comment) {
        CommenterInfo commenterInfo = getCommenterInfo(comment.getCommenter());
        return buildCommentResponse(comment, commenterInfo);
    }

    private CommentResponseDTO buildCommentResponse(Comment comment, CommenterInfo commenterInfo) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getRating(),
                comment.getDate(),
                comment.getStatus().name().toLowerCase(),
                comment.getCommenter() != null ? comment.getCommenter().getId() : null,
                commenterInfo.firstName,
                commenterInfo.lastName,
                commenterInfo.profilePicture,
                comment.getService() != null ? comment.getService().getName() : "N/A",
                comment.getService() != null && comment.getService().getProvider() != null
                        ? comment.getService().getProvider().getCompanyName()
                        : "N/A"
        );
    }

    private record CommenterInfo(String firstName, String lastName, String profilePicture) {
    }
}
