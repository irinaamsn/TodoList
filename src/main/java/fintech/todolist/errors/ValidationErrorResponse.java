package fintech.todolist.errors;

import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final List<ErrorDtoResponse> violations;
}
