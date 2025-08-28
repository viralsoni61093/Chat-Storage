package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

/**
 * Entity representing a chat message within a chat session.
 */
@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    /**
     * Unique identifier for the chat message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The chat session to which this message belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private ChatSession session;

    /**
     * The sender of the message.
     */
    private String sender;
    /**
     * The content of the message.
     */
    private String content;
    /**
     * Optional context for the message.
     */
    private String context;
    /**
     * Timestamp when the message was created.
     */
    @CreatedDate
    private Instant createdAt = Instant.now();
}
