package fintech.todolist.api.services;

import fintech.todolist.api.dto.TodolistDto;

import java.util.List;
import java.util.UUID;

public interface TodolistService {
    void deleteTodolist(UUID todolistId);

    TodolistDto findTodolistById(UUID todolistId);

    void updateTodolist(UUID listId, TodolistDto todolistDto);

    void createTodolist(TodolistDto todolistDto);

    List<TodolistDto> getAllTodolistDtoByUser();
    }
