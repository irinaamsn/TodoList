package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.exceptions.MapperCovertException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TodoListMapperImplTest {
    @InjectMocks
    private TodoListMapperImpl todoListMapper;

    @Test
    void convertTodoListDtoToTodoList() {
        TodolistDto todolistDto = new TodolistDto();
        todolistDto.setTitle("TodolistTitle");

        Todolist result = todoListMapper.convertTodoListDtoToTodoList(todolistDto);

        assertNotNull(result);
        assertEquals(todolistDto.getTitle(), result.getTitle());
    }

    @Test
    void handleException_nullTodolistDto_convertTodoListDtoToTodoList() {
        TodolistDto todolistDto = null;
        assertThrows(MapperCovertException.class, () -> {todoListMapper.convertTodoListDtoToTodoList(todolistDto);});
    }

    @Test
    void convertTodolistToTodolistDto() {
        Todolist todoList = new Todolist();
        todoList.setId(UUID.randomUUID());
        todoList.setTitle("TodolistTitle");

        TodolistDto result = todoListMapper.convertTodolistToTodolistDto(todoList);

        assertNotNull(result);
        assertEquals(todoList.getId(), result.getId());
        assertEquals(todoList.getTitle(), result.getTitle());
    }

    @Test
    void handleException_nullTodolist_convertTodolistToTodolistDto() {
        Todolist todolist = null;
        assertThrows(MapperCovertException.class, () -> {todoListMapper.convertTodolistToTodolistDto(todolist);});
    }
}