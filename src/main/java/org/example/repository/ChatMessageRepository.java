package org.example.repository;

import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ChatMessage entities.
 * Provides methods for querying chat messages by session.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    /**
     * Finds chat messages for a given session where the session is not deleted.
     * @param session the chat session
     * @param pageable pagination information
     * @return a page of ChatMessage entities
     */
    Page<ChatMessage> findBySessionAndSession_DeletedAtIsNull(ChatSession session, Pageable pageable);
}
