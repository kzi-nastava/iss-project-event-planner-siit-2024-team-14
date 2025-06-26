package edu.ftn.iss.eventplanner.dtos.messaging;

import lombok.Data;

import java.util.List;

@Data
public class ChatDTO {
    private Integer id;
    private Object sender, recipient; // maybe change to participant1 and participant2
    private List<ChatMessageDTO> messages;
}
