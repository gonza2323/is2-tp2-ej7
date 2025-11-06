package tinder.tindermascotas.exceptions;

public class ErrorService extends RuntimeException {
    public ErrorService(String message) {
        super(message);
    }
}
