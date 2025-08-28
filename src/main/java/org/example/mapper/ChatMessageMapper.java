package org.example.mapper;

import org.example.model.ChatMessage;
import org.example.dto.ChatMessageRequest;
import org.example.dto.ChatMessageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessageResponse toDto(ChatMessage entity);
    ChatMessage toEntity(ChatMessageRequest dto);
}
