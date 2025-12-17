package com.iaproject.agent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Test de contexto de la aplicación.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.ai.openai.api-key=test-key"
})
class IaMsaDmAgentApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring carga correctamente
    }
}
