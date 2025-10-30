package tinder.tindermascotas.exceptions;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BusinessException extends RuntimeException {
    private final String messageKey;

    @Setter
    private String viewName; // mutable

    public BusinessException(String messageKey) {
        this.messageKey = messageKey;
    }
}