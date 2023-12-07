package fintech.todolist.exceptions.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class GlobalTodolistException extends RuntimeException{
    private final HttpStatus status;
    private final String errorMessage;
    private final long timestamp;
}
