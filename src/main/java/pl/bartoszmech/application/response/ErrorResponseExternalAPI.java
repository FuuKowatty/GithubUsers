package pl.bartoszmech.application.response;

public record ErrorResponseExternalAPI(
        Integer statusCode,
        String message
) {}
