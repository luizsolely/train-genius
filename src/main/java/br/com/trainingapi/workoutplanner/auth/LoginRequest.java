package br.com.trainingapi.workoutplanner.auth;

public record LoginRequest(

    String email,
    String password

)

{}
