package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.exceptions.MapperCovertException;
import fintech.todolist.mappers.TodoListMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TodoListMapperImpl implements TodoListMapper {
    @Override
    public Todolist convertTodoListDtoToTodoList(TodolistDto todoListDto) {
        if ( todoListDto == null ) {
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data todolist", System.currentTimeMillis());
        }
        Todolist todoList = new Todolist();
        todoList.setTitle( todoListDto.getTitle() );
        return todoList;
    }

    @Override
    public TodolistDto convertTodolistToTodolistDto(Todolist todoList) {
        if ( todoList == null ) {
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data todolist", System.currentTimeMillis());
        }
        TodolistDto todoListDto = new TodolistDto();
        todoListDto.setId(todoList.getId());
        todoListDto.setTitle( todoList.getTitle() );
        return todoListDto;
    }
}
