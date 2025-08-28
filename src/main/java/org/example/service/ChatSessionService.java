package org.example.service;

import org.example.model.ChatSession;
import org.example.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing chat sessions.
 * Provides methods to create, retrieve, update, favorite, and delete sessions.
 */
@Service
public class ChatSessionService {
    /**
     * Repository for chat sessions.
     */
    @Autowired
    private ChatSessionRepository chatSessionRepository;

    /**
     * Creates a new chat session for a user.
     * @param userId the user ID
     * @param name the name of the session
     * @return the created ChatSession
     */
    public ChatSession createSession(String userId, String name) {
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .name(name)
                .isFavorite(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return chatSessionRepository.save(session);
    }

    /**
     * Retrieves chat sessions for a user, optionally filtered by favorite status.
     * @param userId the user ID
     * @param favorite optional filter for favorite sessions
     * @return a list of ChatSession entities
     */
    public List<ChatSession> getSessions(String userId, Boolean favorite) {
        if (favorite != null && favorite) {
            return chatSessionRepository.findByUserIdAndIsFavoriteTrueAndDeletedAtIsNull(userId);
        }
        return chatSessionRepository.findByUserIdAndDeletedAtIsNull(userId);
    }

    /**
     * Retrieves a chat session by ID if not deleted.
     * @param id the session ID
     * @return an Optional containing the ChatSession if found and not deleted
     */
    public Optional<ChatSession> getSession(Long id) {
        return chatSessionRepository.findById(id)
                .filter(s -> s.getDeletedAt() == null);
    }

    /**
     * Renames a chat session.
     * @param id the session ID
     * @param newName the new name for the session
     * @return the updated ChatSession
     */
    @Transactional
    public ChatSession renameSession(Long id, String newName) {
        ChatSession session = chatSessionRepository.findById(id).orElseThrow();
        session.setName(newName);
        session.setUpdatedAt(Instant.now());
        return chatSessionRepository.save(session);
    }

    /**
     * Sets the favorite status of a chat session.
     * @param id the session ID
     * @param favorite the favorite status to set
     * @return the updated ChatSession
     */
    @Transactional
    public ChatSession setFavorite(Long id, boolean favorite) {
        ChatSession session = chatSessionRepository.findById(id).orElseThrow();
        session.setIsFavorite(favorite);
        session.setUpdatedAt(Instant.now());
        return chatSessionRepository.save(session);
    }

    /**
     * Marks a chat session as deleted.
     * @param id the session ID
     */
    @Transactional
    public void deleteSession(Long id) {
        ChatSession session = chatSessionRepository.findById(id).orElseThrow();
        session.setDeletedAt(Instant.now());
        chatSessionRepository.save(session);
    }
}
