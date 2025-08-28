package org.example.repository;

import org.example.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing ChatSession entities.
 * Provides methods for querying chat sessions by user and favorite status.
 */
@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    /**
     * Finds favorite chat sessions for a user that are not deleted.
     * @param userId the user ID
     * @return a list of favorite ChatSession entities
     */
    List<ChatSession> findByUserIdAndIsFavoriteTrueAndDeletedAtIsNull(String userId);
    /**
     * Finds all chat sessions for a user that are not deleted.
     * @param userId the user ID
     * @return a list of ChatSession entities
     */
    List<ChatSession> findByUserIdAndDeletedAtIsNull(String userId);
}
