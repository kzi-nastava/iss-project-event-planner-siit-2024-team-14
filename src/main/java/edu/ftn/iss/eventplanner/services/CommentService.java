package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.ApproveCommentDTO;
import edu.ftn.iss.eventplanner.dtos.CommentResponseDTO;
import edu.ftn.iss.eventplanner.dtos.CreateCommentDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.CommentRepository;
import edu.ftn.iss.eventplanner.repositories.ProductRepository;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    // Kreiranje novog komentara sa statusom 'PENDING'
    public CommentResponseDTO createComment(CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();
        comment.setContent(createCommentDTO.getContent());
        comment.setRating(createCommentDTO.getRating());
        comment.setDate(LocalDate.now());
        comment.setStatus(Status.PENDING);

        // Povezivanje sa proizvodom ako postoji productId
        if (createCommentDTO.getProductId() != null) {
            Product product = productRepository.findById(createCommentDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            comment.setProduct(product);
        }

        // Povezivanje sa uslugom ako postoji serviceId
        if (createCommentDTO.getServiceId() != null) {
            Service service = serviceRepository.findById(createCommentDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            comment.setService(service);
        }

        // Povezivanje komentatora (pretpostavljamo da postoji commenterId)
        if (createCommentDTO.getCommenterId() != null) {
            User commenter = userRepository.findById(createCommentDTO.getCommenterId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            comment.setCommenter(commenter);
        }

        Comment savedComment = commentRepository.save(comment);

        // Priprema informacija o komentatoru
        String commenterFirstName = "Anonymous";
        String commenterLastName = "";
        String commenterProfilePicture = null;

        if (savedComment.getCommenter() != null) {
            if (savedComment.getCommenter() instanceof EventOrganizer) {
                EventOrganizer organizer = (EventOrganizer) savedComment.getCommenter();
                commenterFirstName = organizer.getName();
                commenterLastName = organizer.getSurname();
                commenterProfilePicture = organizer.getProfilePhoto();
            } else if (savedComment.getCommenter() instanceof ServiceAndProductProvider) {
                ServiceAndProductProvider provider = (ServiceAndProductProvider) savedComment.getCommenter();
                commenterFirstName = provider.getCompanyName();
            }
        }

        return new CommentResponseDTO(
                savedComment.getId().longValue(),
                savedComment.getContent(),
                savedComment.getRating(),
                savedComment.getDate(),
                savedComment.getStatus().name().toLowerCase(),
                commenterFirstName,
                commenterLastName,
                commenterProfilePicture,
                savedComment.getService() != null ? savedComment.getService().getName() : "N/A",
                savedComment.getService() != null && savedComment.getService().getProvider() != null ?
                        savedComment.getService().getProvider().getCompanyName() : "N/A"
        );
    }

    // Odobravanje ili logičko brisanje komentara
    public CommentResponseDTO approveOrDeleteComment(ApproveCommentDTO approveCommentDTO) {
        Comment comment = commentRepository.findById(approveCommentDTO.getCommentId().intValue())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (approveCommentDTO.isApproved()) {
            comment.setStatus(Status.APPROVED);
        } else {
            comment.setStatus(Status.DELETED);  // Logičko brisanje
        }

        Comment updatedComment = commentRepository.save(comment);

        // Priprema informacija o komentatoru
        String commenterFirstName = "Anonymous";
        String commenterLastName = "";
        String commenterProfilePicture = null;

        if (updatedComment.getCommenter() != null) {
            if (updatedComment.getCommenter() instanceof EventOrganizer) {
                EventOrganizer organizer = (EventOrganizer) updatedComment.getCommenter();
                commenterFirstName = organizer.getName();
                commenterLastName = organizer.getSurname();
                commenterProfilePicture = organizer.getProfilePhoto();
            } else if (updatedComment.getCommenter() instanceof ServiceAndProductProvider) {
                ServiceAndProductProvider provider = (ServiceAndProductProvider) updatedComment.getCommenter();
                commenterFirstName = provider.getCompanyName();  // Kompanija kao ime
                commenterLastName = "";  // Nema prezime
            }
        }

        return new CommentResponseDTO(
                updatedComment.getId().longValue(),
                updatedComment.getContent(),
                updatedComment.getRating(),
                updatedComment.getDate(),
                updatedComment.getStatus().name().toLowerCase(),
                commenterFirstName,
                commenterLastName,
                commenterProfilePicture,
                updatedComment.getService() != null ? updatedComment.getService().getName() : "N/A",
                updatedComment.getService() != null && updatedComment.getService().getProvider() != null ?
                        updatedComment.getService().getProvider().getCompanyName() : "N/A"
        );
    }


    // Dohvatanje svih komentara (za admina)
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(comment -> {
                    String commenterFirstName = "Anonymous";
                    String commenterLastName = "";
                    String commenterProfilePicture = null;

                    if (comment.getCommenter() != null) {
                        if (comment.getCommenter() instanceof EventOrganizer) {
                            EventOrganizer organizer = (EventOrganizer) comment.getCommenter();
                            commenterFirstName = organizer.getName();
                            commenterLastName = organizer.getSurname();
                            commenterProfilePicture = organizer.getProfilePhoto();
                        } else if (comment.getCommenter() instanceof ServiceAndProductProvider) {
                            ServiceAndProductProvider provider = (ServiceAndProductProvider) comment.getCommenter();
                            commenterFirstName = provider.getCompanyName();  // Kompanija kao "ime"
                            commenterLastName = "";  // Nema prezime
                        }
                    }

                    return new CommentResponseDTO(
                            comment.getId().longValue(),
                            comment.getContent(),
                            comment.getRating(),
                            comment.getDate(),
                            comment.getStatus().name().toLowerCase(),
                            commenterFirstName,
                            commenterLastName,
                            commenterProfilePicture,
                            comment.getService() != null ? comment.getService().getName() : "N/A",
                            comment.getService() != null && comment.getService().getProvider() != null ?
                                    comment.getService().getProvider().getCompanyName() : "N/A"
                    );
                })


                .collect(Collectors.toList());
    }

    public List<CommentResponseDTO> getPendingComments() {
        List<Comment> comments = commentRepository.findByStatus(Status.PENDING);

        return comments.stream()
                .map(comment -> {
                    String commenterFirstName = "Anonymous";
                    String commenterLastName = "";
                    String commenterProfilePicture = null;

                    if (comment.getCommenter() != null) {
                        if (comment.getCommenter() instanceof EventOrganizer) {
                            EventOrganizer organizer = (EventOrganizer) comment.getCommenter();
                            commenterFirstName = organizer.getName();
                            commenterLastName = organizer.getSurname();
                            commenterProfilePicture = organizer.getProfilePhoto();
                        } else if (comment.getCommenter() instanceof ServiceAndProductProvider) {
                            ServiceAndProductProvider provider = (ServiceAndProductProvider) comment.getCommenter();
                            commenterFirstName = provider.getCompanyName();  // Kompanija kao "ime"
                            commenterLastName = "";  // Nema prezime
                        }
                    }

                    return new CommentResponseDTO(
                            comment.getId().longValue(),
                            comment.getContent(),
                            comment.getRating(),
                            comment.getDate(),
                            comment.getStatus().name().toLowerCase(),
                            commenterFirstName,
                            commenterLastName,
                            commenterProfilePicture,
                            comment.getService() != null ? comment.getService().getName() : "N/A",
                            comment.getService() != null && comment.getService().getProvider() != null ?
                                    comment.getService().getProvider().getCompanyName() : "N/A"
                    );
                })


                .collect(Collectors.toList());
    }

    // Dohvatanje komentara koji su odobreni (za korisnike)
    public List<CommentResponseDTO> getApprovedComments() {
        List<Comment> comments = commentRepository.findByStatus(Status.APPROVED);

        return comments.stream()
                .map(comment -> {
                    String commenterFirstName = "Anonymous";
                    String commenterLastName = "";
                    String commenterProfilePicture = null;

                    if (comment.getCommenter() != null) {
                        if (comment.getCommenter() instanceof EventOrganizer) {
                            EventOrganizer organizer = (EventOrganizer) comment.getCommenter();
                            commenterFirstName = organizer.getName();
                            commenterLastName = organizer.getSurname();
                            commenterProfilePicture = organizer.getProfilePhoto();
                        } else if (comment.getCommenter() instanceof ServiceAndProductProvider) {
                            ServiceAndProductProvider provider = (ServiceAndProductProvider) comment.getCommenter();
                            commenterFirstName = provider.getCompanyName();  // Kompanija kao ime
                            commenterLastName = "";  // Nema prezime
                        }

                    }

                    return new CommentResponseDTO(
                            comment.getId().longValue(),
                            comment.getContent(),
                            comment.getRating(),
                            comment.getDate(),
                            comment.getStatus().name().toLowerCase(),
                            commenterFirstName,
                            commenterLastName,
                            commenterProfilePicture,
                            comment.getService() != null ? comment.getService().getName() : "N/A",
                            comment.getService() != null && comment.getService().getProvider() != null ?
                                    comment.getService().getProvider().getCompanyName() : "N/A"
                    );
                })
                .collect(Collectors.toList());
    }



}
