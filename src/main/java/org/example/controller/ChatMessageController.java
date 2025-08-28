package org.example.controller;

import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.example.service.ChatMessageService;
import org.example.service.ChatSessionService;
import org.example.dto.ChatMessageRequest;
import org.example.dto.ChatMessageResponse;
import org.example.mapper.ChatMessageMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing chat messages within chat sessions.
 * Provides endpoints to add and retrieve messages for a given session.
 */
@RestController
@RequestMapping("/sessions/{sessionId}/messages")
public class ChatMessageController {
    /**
     * Service for managing chat sessions.
     */
    @Autowired
    private ChatSessionService chatSessionService;
    /**
     * Service for managing chat messages.
     */
    @Autowired
    private ChatMessageService chatMessageService;
    /**
     * Mapper for converting between ChatMessage entities and ChatMessage DTOs.
     */
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    /**
     * Adds a new message to the specified chat session.
     * @param sessionId the ID of the chat session
     * @param request the chat message request body
     * @return the created ChatMessage
     */
    @PostMapping
    public ResponseEntity<ChatMessageResponse> addMessage(
        @PathVariable Long sessionId,
        @Valid @RequestBody ChatMessageRequest request
    ) {
        ChatSession session = chatSessionService.getSession(sessionId).orElseThrow();
        request.setSession(session);
        ChatMessage savedMessage = chatMessageService.addMessage(request);
        ChatMessageResponse response = chatMessageMapper.toDto(savedMessage);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves paginated messages for the specified chat session.
     * @param sessionId the ID of the chat session
     * @param page the page number (default: 0)
     * @param size the page size (default: 20)
     * @return a page of ChatMessage objects
     */
    @GetMapping
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(@PathVariable Long sessionId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        ChatSession session = chatSessionService.getSession(sessionId).orElseThrow();
        Page<ChatMessage> messages = chatMessageService.getMessages(session, page, size);
        Page<ChatMessageResponse> responses = messages.map(chatMessageMapper::toDto);
        return ResponseEntity.ok(responses);
    }
}
