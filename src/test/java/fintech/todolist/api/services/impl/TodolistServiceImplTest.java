package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.api.repositories.TodolistRepository;
import fintech.todolist.api.services.UserService;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.exceptions.NotFoundException;
import fintech.todolist.mappers.TodoListMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodolistServiceImplTest {
    @Mock
    private TodolistRepository todolistRepository;
    @Mock
    private UserService userService;
    @Mock
    private TodoListMapper todoListMapper;
    @InjectMocks
    private TodolistServiceImpl todolistService;

    @Test
    void deleteTodolist() {
        UUID todolistId = UUID.randomUUID();

        when(todolistRepository.existsById(todolistId)).thenReturn(true);

        assertDoesNotThrow(() -> todolistService.deleteTodolist(todolistId));

        verify(todolistRepository, times(1)).deleteById(todolistId);
        verify(todolistRepository, times(1)).existsById(todolistId);
    }

    @Test
    void handleException_deleteTodolist() {
        UUID todolistId = UUID.randomUUID();

        when(todolistRepository.existsById(todolistId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> todolistService.deleteTodolist(todolistId));

        verify(todolistRepository, times(0)).deleteById(todolistId);
        verify(todolistRepository, times(1)).existsById(todolistId);
    }

    @Test
    void updateTodolist() {
        String title = "titleTodolist";
        UUID todolistId = UUID.randomUUID();
        TodolistDto todolistDto = new TodolistDto(todolistId, title);

        when(todolistRepository.existsById(todolistId)).thenReturn(true);

        assertDoesNotThrow(() -> todolistService.updateTodolist(todolistId, todolistDto));

        verify(todolistRepository, times(1)).existsById(todolistId);
        verify(todolistRepository, times(1)).updateTodolistById(todolistId, todolistDto.getTitle());
    }

    @Test
    void handleException_updateTodolist() {
        String title = "titleTodolist";
        UUID todolistId = UUID.randomUUID();
        TodolistDto todolistDto = new TodolistDto(todolistId, title);

        when(todolistRepository.existsById(todolistId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> todolistService.updateTodolist(todolistId, todolistDto));

        verify(todolistRepository, times(1)).existsById(todolistId);
        verify(todolistRepository, times(0)).updateTodolistById(todolistId, todolistDto.getTitle());
    }

    @Test
    void createTodolist() {
        AppUserDetails user = new AppUserDetails(new AppUser());
        String title = "titleTodolist";
        UUID todolistId = UUID.randomUUID();
        TodolistDto todolistDto = new TodolistDto(todolistId, title);

        when(userService.getAuthUser()).thenReturn(user);
        when(todoListMapper.convertTodoListDtoToTodoList(todolistDto)).thenReturn(new Todolist(todolistId,title,user.getAppUser()));

        assertDoesNotThrow(() -> todolistService.createTodolist(todolistDto));

        verify(todoListMapper, times(1)).convertTodoListDtoToTodoList(todolistDto);
        verify(todolistRepository, times(1)).save(todoListMapper.convertTodoListDtoToTodoList(todolistDto));
    }

    @Test
    void getAllTodolistDtoByUser() {
        AppUserDetails user = new AppUserDetails(new AppUser());
        when(userService.getAuthUser()).thenReturn(user);
        when(todolistRepository.findAllByUser(user.getAppUser())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> todolistService.getAllTodolistDtoByUser());

        verify(todolistRepository, times(1)).findAllByUser(user.getAppUser());
    }

    @Test
    void findTodolistById() {
        String title = "titleTodolist";
        UUID todolistId = UUID.randomUUID();
        Todolist todolist = new Todolist();
        todolist.setId(todolistId);
        todolist.setTitle(title);
        when(todolistRepository.findById(todolistId)).thenReturn(Optional.of(todolist));
        when(todoListMapper.convertTodolistToTodolistDto(todolist)).thenReturn(new TodolistDto(todolistId,title));

        TodolistDto result =  todolistService.findTodolistById(todolistId);

        verify(todolistRepository, times(1)).findById(todolistId);
        assertTrue(result.getId().equals(todolistId));
        assertTrue(result.getTitle().equals(title));
    }

    @Test
    void handleException_findTodolistById() {
        UUID todolistId = UUID.randomUUID();

        when(todolistRepository.findById(todolistId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> todolistService.findTodolistById(todolistId));

        verify(todolistRepository, times(1)).findById(todolistId);
    }
}