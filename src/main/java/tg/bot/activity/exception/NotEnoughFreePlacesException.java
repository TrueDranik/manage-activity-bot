package tg.bot.activity.exception;

public class NotEnoughFreePlacesException extends RuntimeException {

    public NotEnoughFreePlacesException(String errorMessage) {
        super(errorMessage);
    }
}
