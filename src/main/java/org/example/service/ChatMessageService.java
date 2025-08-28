package org.example.service;

import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.example.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Service for managing chat messages within chat sessions.
 * Provides methods to add and retrieve messages.
 */
@Service
public class ChatMessageService {
    /**
     * Repository for chat messages.
     */
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    /**
     * Adds a new message to a chat session.
     * @param session the chat session
     * @param sender the sender of the message
     * @param content the message content
     * @param context optional context for the message
     * @return the created ChatMessage
     */
    @Transactional
    public ChatMessage addMessage(ChatSession session, String sender, String content, String context) {
        ChatMessage message = ChatMessage.builder()
                .session(session)
                .sender(sender)
                .content(content)
                .context(context)
                .createdAt(Instant.now())
                .build();
        return chatMessageRepository.save(message);
    }

    /**
     * Retrieves paginated messages for a chat session.
     * @param session the chat session
     * @param page the page number
     * @param size the page size
     * @return a page of ChatMessage entities
     */
    public Page<ChatMessage> getMessages(ChatSession session, int page, int size) {
        return chatMessageRepository.findBySessionAndSession_DeletedAtIsNull(session, PageRequest.of(page, size));
    }
}
