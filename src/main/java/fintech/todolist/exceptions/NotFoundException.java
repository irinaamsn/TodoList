package fintech.todolist.exceptions;


import fintech.todolist.exceptions.base.GlobalTodolistException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GlobalTodolistException {
    public NotFoundException(HttpStatus status, String errorMessage, long timestamp) {
        super(status, errorMessage, timestamp);
    }
}
