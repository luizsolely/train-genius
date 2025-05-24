package com.luizsolely.traingenius.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.luizsolely.traingenius.exception.GeminiClientNotInitializedException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeminiService {

    @Value("${gemini.model.name}")
    private String modelName;

    private Client geminiClient;

    @PostConstruct
    public void init() {
        try {
            this.geminiClient = new Client();
            log.info("✅ GeminiService: Gemini client initialized successfully.");
        } catch (Exception e) {
            log.error("❌ GeminiService: Failed to initialize Gemini client. Check GOOGLE_API_KEY.", e);
            this.geminiClient = null;
        }
    }

    public String generateText(String promptText) {
        if (this.geminiClient == null) {
            log.error("❌ GeminiService: Client not initialized.");
            throw new GeminiClientNotInitializedException();
        }

        try {
            GenerateContentResponse response = this.geminiClient
                    .models
                    .generateContent(this.modelName, promptText, null);

            return response.text();
        } catch (Exception e) {
            log.error("❌ GeminiService: Error while generating content.", e);
            throw new RuntimeException("Error generating content from Gemini AI.", e);
        }
    }
}
