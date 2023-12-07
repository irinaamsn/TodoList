package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.api.repositories.TodolistRepository;
import fintech.todolist.api.services.TodolistService;
import fintech.todolist.api.services.UserService;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.exceptions.NotFoundException;
import fintech.todolist.mappers.TodoListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodolistServiceImpl implements TodolistService {
    private final TodolistRepository todolistRepository;
    private final TodoListMapper todoListMapper;
    private final UserService userService;

    @Transactional
    @Override
    public void deleteTodolist(UUID todolistId) {
        if (!todolistRepository.existsById(todolistId))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "TodoList not found", System.currentTimeMillis());
        todolistRepository.deleteById(todolistId);
    }

    @Transactional
    @Override
    public void updateTodolist(UUID todolistId, TodolistDto todolistDto) {
        if (!todolistRepository.existsById(todolistId))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Todolist not found", System.currentTimeMillis());
        todolistRepository.updateTodolistById(todolistId,todolistDto.getTitle());
    }

    @Override
    public void createTodolist(TodolistDto todolistDto) {
        Todolist todoList = todoListMapper.convertTodoListDtoToTodoList(todolistDto);
        todoList.setUser(userService.getAuthUser().getAppUser());
        todolistRepository.save(todoList);
    }

    @Override
    public List<TodolistDto> getAllTodolistDtoByUser() {
        return todolistRepository.findAllByUser(userService.getAuthUser().getAppUser())
                .stream().map(todoListMapper::convertTodolistToTodolistDto).toList();
    }

    @Override
    public TodolistDto findTodolistById(UUID todolistId) {
        return todolistRepository.findById(todolistId).map(todoListMapper::convertTodolistToTodolistDto)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Todolist not found", System.currentTimeMillis()));
    }
}
