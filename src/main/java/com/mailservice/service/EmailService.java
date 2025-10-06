package com.mailservice.service;

import com.mailservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final WebClient.Builder webClientBuilder;

    @Value("${brevo.api.key}") // Lida a partir do application.properties
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public void sendWelcomeEmail(UserDTO user) {
        WebClient webClient = webClientBuilder.baseUrl("https://api.brevo.com/v3").build();

        String subject = "Seja bem-vindo(a), " + user.getNome() + "!";
        // Você pode criar um template HTML mais elaborado aqui
        String htmlContent = "<html><body><h1>Olá " + user.getNome() + "!</h1><p>Seu cadastro foi realizado com sucesso. Estamos felizes em ter você conosco!</p></body></html>";

        // O corpo da requisição no formato que a API do Brevo espera
        Map<String, Object> body = Map.of(
                "sender", Map.of("email", senderEmail, "name", "Bruno Fraga Dev"),
                "to", List.of(Map.of("email", user.getEmail(), "name", user.getNome())),
                "subject", subject,
                "htmlContent", htmlContent
        );

        // Faz a chamada POST para a API do Brevo
        webClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve() // Executa a requisição
                .toBodilessEntity() // Não nos importamos com o corpo da resposta de sucesso, apenas que deu 2xx
                .block(); // Executa a chamada de forma síncrona

        System.out.println("E-mail de boas-vindas enviado para " + user.getEmail() + " via API Brevo.");
    }
}
