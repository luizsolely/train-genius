package com.luizsolely.traingenius.service;

import com.luizsolely.traingenius.model.enums.AvailableDays;
import com.luizsolely.traingenius.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PromptBuilderService {

    public String buildWorkoutPrompt(User user) {
        String availableDays = user.getAvailableDays().stream()
                .map(AvailableDays::getDescription)
                .collect(Collectors.joining(", "));

        return String.format("""
            You are a professional fitness coach.
            Create a personalized workout plan for the following user:

            Name: %s
            Weight: %.2f kg
            Height: %.2f m
            Training Level: %s
            Goal: %s
            Available Days: %s

            Respond **only** with a JSON object in this exact structure:

            {
              "title": "<Título do plano>",
              "description": "<Descrição geral>",
              "trainingDate": "<YYYY-MM-DD>",
              "exercises": [
                "<Exercício 1>",
                "<Exercício 2>",
                "..."
              ]
            }

            Do not include any additional text, labels, or markdown.
            """,
                user.getName(),
                user.getWeight(),
                user.getHeight(),
                user.getTrainingLevel().name(),
                user.getGoal().name(),
                availableDays
        );
    }
}
