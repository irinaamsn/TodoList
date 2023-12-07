package fintech.todolist.exceptions;

import fintech.todolist.exceptions.base.GlobalTodolistException;
import org.springframework.http.HttpStatus;

public class MapperCovertException extends GlobalTodolistException {
    public MapperCovertException(HttpStatus status, String errorMessage, long timestamp) {
        super(status, errorMessage, timestamp);
    }
}
