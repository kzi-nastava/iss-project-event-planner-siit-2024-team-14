package edu.ftn.iss.eventplanner.dtos.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageDTO {
    private int senderId; //
    @JsonIgnore
    private int recipientId;
    private String content;
}
