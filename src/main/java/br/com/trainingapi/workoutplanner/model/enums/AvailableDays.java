package br.com.trainingapi.workoutplanner.model.enums;

public enum AvailableDays {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String description;

    AvailableDays(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
