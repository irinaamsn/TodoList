package fintech.todolist.mappers;

import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.Todolist;


public interface TodoListMapper {
    Todolist convertTodoListDtoToTodoList(TodolistDto todoListDto);
    TodolistDto convertTodolistToTodolistDto (Todolist todoList);
}
