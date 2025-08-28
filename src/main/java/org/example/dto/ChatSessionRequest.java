package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionRequest {
    private Long id;
    private String userId;
    private String name;
    private Boolean favorite;
}
