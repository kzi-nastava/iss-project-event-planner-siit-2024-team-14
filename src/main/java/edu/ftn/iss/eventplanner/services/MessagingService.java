package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Chat;
import edu.ftn.iss.eventplanner.entities.Message;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ChatRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final ChatRepository chatRepository;
    private final UserService userService;


    @Transactional
    public Message sendMessage(int senderId, int recipientId, String message) {
        try {
            return getChat(senderId, recipientId)
                    .sendMessage(senderId, message);
        }
        catch (NotFoundException | IllegalArgumentException | IllegalStateException e) {
            throw new BadRequestException("Failed to send message: " + e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }


    public List<Chat> getUserInbox(int userId) {
        userService.getUserById(userId);
        return chatRepository.findByParticipant(userId);
    }


    public Chat getChat(int senderId, int recipientId) {
        User sender = userService.getUserById(senderId),
             recipient = userService.getUserById(recipientId);

        return chatRepository.findByParticipant(senderId, recipientId)
                .orElseGet(() -> chatRepository.save(new Chat(sender, recipient)));
    }






    @Transactional
    public Chat getChat(int chatterId) {
        var user = Optional.ofNullable(getCurrentUser())
                .orElseThrow();

        return getChat(user.getId(), chatterId);
    }


    public List<Chat> getUserInbox() {
        var user = Optional.ofNullable(getCurrentUser())
                .orElseThrow();

        return chatRepository.findByParticipant(user.getId());
    }


    @Transactional
    public Message sendMessage(int chatterId, String message) {
        var user = Optional.ofNullable(getCurrentUser())
                .orElseThrow();

        return sendMessage(user.getId(), chatterId, message);
    }


    @Nullable
    private static User getCurrentUser() { // should be in some auth service
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.getPrincipal() instanceof User user) ? user : null;
    }

}
