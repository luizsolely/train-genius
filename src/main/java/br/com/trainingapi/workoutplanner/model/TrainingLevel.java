package br.com.trainingapi.workoutplanner.model;

public enum TrainingLevel {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private String description;

    TrainingLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
