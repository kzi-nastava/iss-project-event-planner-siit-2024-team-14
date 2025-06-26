package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.messaging.ChatDTO;
import edu.ftn.iss.eventplanner.dtos.messaging.SendMessageDTO;
import edu.ftn.iss.eventplanner.dtos.messaging.ChatMessageDTO;
import edu.ftn.iss.eventplanner.services.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@PreAuthorize("isAuthenticated()")
@Controller
@RequiredArgsConstructor
public class DirectMessagingController {

    private final MessagingService messagingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ModelMapper modelMapper;



    @GetMapping(path = "/api/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ChatDTO[] getMyInbox(
            @RequestParam int userId // should determine this from token
    ) {
        return messagingService.getUserInbox(userId).stream()
                .map(c -> modelMapper.map(c, ChatDTO.class)).toArray(ChatDTO[]::new);
    }


    @GetMapping(path = "/api/chat/{recipientId:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ChatDTO getChat(
            @RequestParam int userId, // should determine this from token
            @PathVariable int recipientId
    ) {
        var chat = messagingService.getChat(userId, recipientId);
        return modelMapper.map(chat, ChatDTO.class);
    }


    @MessageMapping("/chat/{recipientId}")
    public void sendDirectMessage(
            @DestinationVariable int recipientId,
            @Payload SendMessageDTO messageDto
    ) {
        messageDto.setRecipientId(recipientId);
        sendMessage(messageDto);
    }


    @PostMapping(path = "/api/chat/{recipientId:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ChatMessageDTO postSendDirectMessage(
            @RequestBody SendMessageDTO messageDto,
            @RequestParam int userId,
            @PathVariable int recipientId
    ) {
        messageDto.setSenderId(userId);
        messageDto.setRecipientId(recipientId);
        return sendMessage(messageDto);
    }


    private ChatMessageDTO sendMessage(SendMessageDTO messageDTO) {
        var sentMessage = messagingService.sendMessage(
                messageDTO.getSenderId(),
                messageDTO.getRecipientId(),
                messageDTO.getContent()
        );

        var sentMessageDto = modelMapper.map(sentMessage, ChatMessageDTO.class);
        messagingTemplate.convertAndSend("/queue/" + messageDTO.getRecipientId(), sentMessageDto);
        messagingTemplate.convertAndSend("/queue/" + messageDTO.getSenderId(), sentMessageDto);
        return sentMessageDto;
    }



    @MessageExceptionHandler
    public void handleMessageException(Throwable ex) {
        log.error("[DirectMessagingController] Message exception: ", ex);
    }

}
