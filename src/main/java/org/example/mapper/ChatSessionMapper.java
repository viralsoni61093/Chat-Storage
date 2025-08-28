package org.example.mapper;

import org.example.model.ChatSession;
import org.example.dto.ChatSessionRequest;
import org.example.dto.ChatSessionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatSessionMapper {
    ChatSessionResponse toDto(ChatSession entity);
    ChatSession toEntity(ChatSessionRequest dto);
}
