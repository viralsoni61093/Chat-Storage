package org.example.controller;

import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.example.service.ChatMessageService;
import org.example.service.ChatSessionService;
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
     * Adds a new message to the specified chat session.
     * @param sessionId the ID of the chat session
     * @param sender the sender of the message
     * @param content the message content
     * @param context optional context for the message
     * @return the created ChatMessage
     */
    @PostMapping
    public ResponseEntity<ChatMessage> addMessage(@PathVariable Long sessionId, @RequestParam String sender, @RequestParam String content, @RequestParam(required = false) String context) {
        ChatSession session = chatSessionService.getSession(sessionId).orElseThrow();
        return ResponseEntity.ok(chatMessageService.addMessage(session, sender, content, context));
    }

    /**
     * Retrieves paginated messages for the specified chat session.
     * @param sessionId the ID of the chat session
     * @param page the page number (default: 0)
     * @param size the page size (default: 20)
     * @return a page of ChatMessage objects
     */
    @GetMapping
    public ResponseEntity<Page<ChatMessage>> getMessages(@PathVariable Long sessionId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        ChatSession session = chatSessionService.getSession(sessionId).orElseThrow();
        return ResponseEntity.ok(chatMessageService.getMessages(session, page, size));
    }
}
