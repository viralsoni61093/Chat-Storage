package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

/**
 * Entity representing a chat session.
 * Contains metadata and a list of associated chat messages.
 */
@Entity
@Table(name = "chat_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {
    /**
     * Unique identifier for the chat session.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user ID associated with this session.
     */
    private String userId;
    /**
     * The name of the chat session.
     */
    private String name;
    /**
     * Indicates if the session is marked as favorite.
     */
    private Boolean isFavorite = false;
    /**
     * Timestamp when the session was created.
     */
    private Instant createdAt = Instant.now();
    /**
     * Timestamp when the session was last updated.
     */
    private Instant updatedAt = Instant.now();
    /**
     * Timestamp when the session was deleted (if applicable).
     */
    private Instant deletedAt;

    /**
     * List of messages associated with this session.
     */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ChatMessage> messages;
}
