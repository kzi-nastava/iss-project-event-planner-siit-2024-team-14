package edu.ftn.iss.eventplanner.dtos.messaging;

import edu.ftn.iss.eventplanner.entities.Chat;
import edu.ftn.iss.eventplanner.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class ChatMessageDTO {
    private int chatId;
    private int senderId;
    private String content;
    private LocalDateTime timestamp;


    public void setSender(@NotNull User sender) {
        this.senderId = Objects.requireNonNull(sender).getId();
    }

    public void setChat(@NotNull Chat chat) {
        this.chatId = Objects.requireNonNull(chat).getId();
    }

}
