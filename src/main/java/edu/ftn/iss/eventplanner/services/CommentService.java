package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.comments.ApproveCommentDTO;
import edu.ftn.iss.eventplanner.dtos.comments.CommentResponseDTO;
import edu.ftn.iss.eventplanner.dtos.comments.CreateCommentDTO;
import edu.ftn.iss.eventplanner.dtos.notifications.NotificationDTO;
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

    /**
     * Creates a new comment with status PENDING and associates it with a product or service.
     */
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

    /**
     * Marks a comment as DELETED (rejected) and notifies the user.
     */
    public CommentResponseDTO deleteComment(ApproveCommentDTO dto) {
        Comment comment = findCommentById(dto.getCommentId());
        comment.setStatus(Status.DELETED);
        commentRepository.save(comment);

        String message = "Your comment: '" + comment.getContent() + "' for '" + getEntityName(comment) + "' has been rejected!";
        sendNotification(comment.getCommenter().getId(), message, comment.getId());

        CommenterInfo commenterInfo = getCommenterInfo(comment.getCommenter());
        return buildCommentResponse(comment, commenterInfo);
    }

    /**
     * Approves a comment and sends a notification to both the commenter and the provider.
     */
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

    /**
     * Retrieves all comments (regardless of status).
     */
    public List<CommentResponseDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only comments that are currently pending approval.
     */
    public List<CommentResponseDTO> getPendingComments() {
        return commentRepository.findByStatus(Status.PENDING).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only approved comments.
     */
    public List<CommentResponseDTO> getApprovedComments() {
        return commentRepository.findByStatus(Status.APPROVED).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Helper methods ---

    /**
     * Finds a comment by ID or throws an exception if not found.
     */
    private Comment findCommentById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    /**
     * Finds a product by ID or throws an exception if not found.
     */
    private Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /**
     * Finds a service by ID or throws an exception if not found.
     */
    private edu.ftn.iss.eventplanner.entities.Service findServiceById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    /**
     * Finds a user by ID or throws an exception if not found.
     */
    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Sends a real-time notification to the given user via WebSocket and persists it.
     */
    private void sendNotification(Integer userId, String message, Integer commentId) {
        NotificationDTO notification = notificationService.createNotification(userId, message, commentId, null);
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
        notificationWebSocketService.sendWebSocketNotification(userId, notification);
    }

    /**
     * Notifies the provider (of a product or service) that a new comment has been posted on their item.
     */
    private void notifyProvider(Comment comment, String entityName) {
        User provider = (comment.getProduct() != null) ? comment.getProduct().getProvider()
                : (comment.getService() != null) ? comment.getService().getProvider()
                : null;

        if (provider != null) {
            String message = "User: " + comment.getCommenter().getEmail() + " commented on your " + entityName + ": '" + comment.getContent() + "'";
            sendNotification(provider.getId(), message, comment.getId());
        }
    }

    /**
     * Retrieves the name of the commented entity (product or service).
     */
    private String getEntityName(Comment comment) {
        if (comment.getProduct() != null) return comment.getProduct().getName();
        if (comment.getService() != null) return comment.getService().getName();
        return "N/A";
    }

    /**
     * Retrieves the provider's company name associated with the comment.
     */
    private String getProvider(Comment comment) {
        if (comment.getService() != null && comment.getService().getProvider() != null) {
            return comment.getService().getProvider().getCompanyName();
        }
        if (comment.getProduct() != null && comment.getProduct().getProvider() != null) {
            return comment.getProduct().getProvider().getCompanyName();
        }
        return "N/A";
    }

    /**
     * Constructs commenter information based on user type (organizer or provider).
     */
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

    /**
     * Maps a Comment entity to CommentResponseDTO.
     */
    private CommentResponseDTO mapToDTO(Comment comment) {
        CommenterInfo commenterInfo = getCommenterInfo(comment.getCommenter());
        return buildCommentResponse(comment, commenterInfo);
    }

    /**
     * Builds a CommentResponseDTO using comment data and commenter info.
     */
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
                "profile-photos/" +commenterInfo.profilePicture,
                getEntityName(comment),
                getProvider(comment)
        );
    }

    /**
     * Compact record to hold basic commenter information.
     */
    private record CommenterInfo(String firstName, String lastName, String profilePicture) {
    }
}
