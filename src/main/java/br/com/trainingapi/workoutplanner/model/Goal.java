package br.com.trainingapi.workoutplanner.model;

public enum Goal {
    WEIGHT_LOSS("Weight Loss"),
    HYPERTROPHY("Hypertrophy"),
    ENDURANCE("Endurance"),
    MAINTENANCE("Maintenance");

    private String description;

    Goal(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
