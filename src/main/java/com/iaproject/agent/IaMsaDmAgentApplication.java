package com.iaproject.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del servicio de agente IA.
 * Implementa Spring AI para facilitar la integración con modelos de lenguaje.
 */
@SpringBootApplication
public class IaMsaDmAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(IaMsaDmAgentApplication.class, args);
    }
}
