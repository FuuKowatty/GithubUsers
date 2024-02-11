package pl.bartoszmech.application.response;

public record ErrorResponse(
        Integer statusCode,
        String message
) {}
