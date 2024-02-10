package pl.bartoszmech.infrastructure.fetcher;

public class ExternalAPIException extends RuntimeException {

    public Integer statusCode;

    public ExternalAPIException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
