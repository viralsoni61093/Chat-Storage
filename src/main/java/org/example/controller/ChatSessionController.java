package org.example.controller;

import org.example.dto.ChatSessionRequest;
import org.example.dto.ChatSessionResponse;
import org.example.model.ChatSession;
import org.example.service.ChatSessionService;
import org.example.mapper.ChatSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing chat sessions.
 * Provides endpoints to create, retrieve, update, and delete chat sessions.
 */
@RestController
@RequestMapping("/sessions")
public class ChatSessionController {
    /**
     * Service for managing chat sessions.
     */
    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    /**
     * Creates a new chat session for a user.
     * @param request the chat session request body
     * @return the created ChatSessionResponse
     */
    @PostMapping
    public ResponseEntity<ChatSessionResponse> createSession(@Valid @RequestBody ChatSessionRequest request) {
        ChatSession savedSession = chatSessionService.createSession(request);
        ChatSessionResponse response = chatSessionMapper.toDto(savedSession);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves chat sessions for a user, optionally filtered by favorite status.
     * @param userId the user ID
     * @param favorite optional filter for favorite sessions
     * @return a list of ChatSessionResponse objects
     */
    @GetMapping
    public ResponseEntity<List<ChatSessionResponse>> getSessions(@RequestParam String userId, @RequestParam(required = false) Boolean favorite) {
        List<ChatSessionResponse> responses = chatSessionService.getSessions(userId, favorite)
            .stream()
            .map(chatSessionMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Renames a chat session.
     * @param id the session ID
     * @param name the new name for the session
     * @return the updated ChatSession
     */
    @PatchMapping("/{id}/rename")
    public ResponseEntity<ChatSession> renameSession(@PathVariable Long id, @RequestParam String name) {
        return ResponseEntity.ok(chatSessionService.renameSession(id, name));
    }

    /**
     * Sets the favorite status of a chat session.
     * @param id the session ID
     * @param favorite the favorite status to set
     * @return the updated ChatSession
     */
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ChatSession> setFavorite(@PathVariable Long id, @RequestParam boolean favorite) {
        return ResponseEntity.ok(chatSessionService.setFavorite(id, favorite));
    }

    /**
     * Deletes a chat session.
     * @param id the session ID
     * @return HTTP 204 No Content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        chatSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
