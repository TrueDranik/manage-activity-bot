package tg.bot.activity.api.rest;

import com.bot.sup.exception.NotEnoughFreePlacesException;
import com.bot.sup.exception.NotValidParamParticipantsException;
import com.bot.sup.model.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
@ResponseBody
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotValidParamParticipantsException.class)
    public ResponseEntity<Object> handleNotValidParamParticipants(NotValidParamParticipantsException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotEnoughFreePlacesException.class)
    public ResponseEntity<Object> handleNotEnoughFreePlacesException(NotEnoughFreePlacesException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignStatusException(FeignException exception, HttpServletResponse response) {
        var status = response.getStatus();
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.valueOf(status));
    }

    private ErrorResponse getErrorResponse(Exception exception) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .build();
    }
}
