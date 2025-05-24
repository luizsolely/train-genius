package com.luizsolely.traingenius.exception;

public class GeminiClientNotInitializedException extends RuntimeException {
    public GeminiClientNotInitializedException() {
        super("Gemini client is not initialized. Check your GOOGLE_API_KEY.");
    }
}
