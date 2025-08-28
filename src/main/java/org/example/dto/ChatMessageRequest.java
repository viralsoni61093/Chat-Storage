package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.model.ChatSession;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private Long id;
    private ChatSession session;
    private String sender;
    private String content;
    private String context;
    private Instant createdAt;
}
