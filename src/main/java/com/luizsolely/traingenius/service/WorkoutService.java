package com.luizsolely.traingenius.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizsolely.traingenius.dto.WorkoutGeneratedResponse;
import com.luizsolely.traingenius.dto.WorkoutRequest;
import com.luizsolely.traingenius.dto.WorkoutResponse;
import com.luizsolely.traingenius.exception.AiGenerationException;
import com.luizsolely.traingenius.exception.UserNotFoundException;
import com.luizsolely.traingenius.exception.WorkoutNotFoundException;
import com.luizsolely.traingenius.mapper.WorkoutMapper;
import com.luizsolely.traingenius.model.User;
import com.luizsolely.traingenius.model.Workout;
import com.luizsolely.traingenius.repository.UserRepository;
import com.luizsolely.traingenius.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;
    private final PromptBuilderService promptBuilderService;
    private final ObjectMapper objectMapper;
    private final WorkoutMapper workoutMapper;

    public WorkoutResponse generateWorkout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        String prompt = promptBuilderService.buildWorkoutPrompt(user);
        String rawResponse;
        try {
            rawResponse = geminiService.generateText(prompt);
        } catch (Exception e) {
            log.error("❌ Error during AI generation for user {}", userId, e);
            throw new AiGenerationException("Failed to generate workout from AI");
        }

        // Clean AI response: strip markdown fences and extract JSON object
        String jsonResponse = rawResponse;
        int start = jsonResponse.indexOf('{');
        int end = jsonResponse.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            jsonResponse = jsonResponse.substring(start, end + 1);
        } else {
            log.error("❌ Unable to locate JSON object in AI response for user {}: {}", userId, rawResponse);
            throw new AiGenerationException("AI response did not contain a valid JSON object");
        }

        WorkoutGeneratedResponse generated;
        try {
            generated = objectMapper.readValue(jsonResponse, WorkoutGeneratedResponse.class);
        } catch (JsonProcessingException e) {
            log.error("❌ Error parsing AI response JSON for user {}: {}", userId, e.getMessage(), e);
            throw new AiGenerationException("Invalid AI response format");
        }

        // Use MapStruct to map AI response to entity
        Workout workout = workoutMapper.toEntity(generated);
        workout.setUser(user);

        Workout savedWorkout = workoutRepository.save(workout);
        log.info("✅ Workout generated and saved for user {}", userId);

        return toResponse(savedWorkout);
    }

    public WorkoutResponse createWorkout(Long userId, WorkoutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Workout workout = workoutMapper.toEntity(request);
        workout.setUser(user);

        Workout savedWorkout = workoutRepository.save(workout);
        log.info("✅ Workout manually created for user {}", userId);

        return toResponse(savedWorkout);
    }

    public List<WorkoutResponse> getAllWorkoutsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return workoutRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public WorkoutResponse getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));

        return toResponse(workout);
    }

    public void deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new WorkoutNotFoundException(id);
        }

        workoutRepository.deleteById(id);
        log.info("🗑️ Workout with id {} deleted.", id);
    }

    private WorkoutResponse toResponse(Workout workout) {
        return workoutMapper.toResponse(workout);
    }
}
