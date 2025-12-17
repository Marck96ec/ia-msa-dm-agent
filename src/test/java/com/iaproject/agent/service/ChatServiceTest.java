package com.iaproject.agent.service;

import com.iaproject.agent.dto.ChatRequest;
import com.iaproject.agent.dto.ChatResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests unitarios para ChatService.
 */
@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatClient chatClient;

    @InjectMocks
    private ChatService chatService;

    @Test
    void testServiceInjection() {
        assertNotNull(chatService);
    }

    @Test
    void testChatRequestBuilder() {
        ChatRequest request = ChatRequest.builder()
                .message("Test message")
                .temperature(0.7)
                .maxTokens(100)
                .build();

        assertThat(request.getMessage()).isEqualTo("Test message");
        assertThat(request.getTemperature()).isEqualTo(0.7);
        assertThat(request.getMaxTokens()).isEqualTo(100);
    }
}
