package pl.bartoszmech.application.response;

public class ExternalAPIResponse extends RuntimeException{

    public Integer statusCode;

    public ExternalAPIResponse(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
