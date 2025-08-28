package org.example.service;

import org.example.model.ChatSession;
import org.example.repository.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatSessionServiceTest {
    @Mock
    private ChatSessionRepository chatSessionRepository;

    @InjectMocks
    private ChatSessionService chatSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSession() {
        ChatSession session = ChatSession.builder().userId("user1").name("Test").build();
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(session);
        ChatSession created = chatSessionService.createSession("user1", "Test");
        assertEquals("user1", created.getUserId());
        assertEquals("Test", created.getName());
    }

    @Test
    void testGetSession() {
        ChatSession session = ChatSession.builder().id(1L).userId("user1").name("Test").build();
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        Optional<ChatSession> found = chatSessionService.getSession(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }
}

