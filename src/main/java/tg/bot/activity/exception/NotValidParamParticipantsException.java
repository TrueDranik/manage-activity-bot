package tg.bot.activity.exception;

public class NotValidParamParticipantsException extends RuntimeException{

    public NotValidParamParticipantsException(String errorMessage) {
        super(errorMessage);
    }
}
