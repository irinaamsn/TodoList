package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Task;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.api.repositories.TaskRepository;
import fintech.todolist.api.repositories.TodolistRepository;
import fintech.todolist.api.services.UserService;
import fintech.todolist.api.services.impl.TaskServiceImpl;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.exceptions.NotFoundException;
import fintech.todolist.mappers.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TodolistRepository todolistRepository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void deleteTaskTest() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.existsById(taskId)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(taskId));

        verify(taskRepository, times(1)).deleteById(taskId);
        verify(taskRepository, times(1)).existsById(taskId);
    }

    @Test
    void handleException_deleteTaskTest() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(taskId));

        verify(taskRepository, times(0)).deleteById(taskId);
        verify(taskRepository, times(1)).existsById(taskId);
    }

    @Test
    void updateTaskTest() {
        String title = "titleTasks";
        String details = "detailTask";
        LocalDateTime dateTime = LocalDateTime.of(2023, 01, 01, 10, 00);
        UUID taskId = UUID.randomUUID();
        TaskDto taskDto = new TaskDto(title, details, dateTime);

        when(taskRepository.existsById(taskId)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.updateTask(taskId, taskDto));

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).updateById(taskId, taskDto.getTitle(), taskDto.getDetails(),
                taskDto.getDeadline());
    }

    @Test
    void handleException_updateTaskTest() {
        String title = "titleTasks";
        String details = "detailTask";
        LocalDateTime dateTime = LocalDateTime.of(2023, 01, 01, 10, 00);
        UUID taskId = UUID.randomUUID();
        TaskDto taskDto = new TaskDto(title, details, dateTime);

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> taskService.updateTask(taskId, taskDto));

        verify(taskRepository, times(1)).existsById(taskId);
        assertTrue(taskDto.getDateTimeLastChange().equals(dateTime));
        verify(taskRepository, times(0)).updateById(taskId, taskDto.getTitle(), taskDto.getDetails(),
                 taskDto.getDeadline());
    }

    @Test
    void createTaskTest() {
        String title = "titleTasks";
        String details = "detailTask";
        UUID todolistId = UUID.randomUUID();
        TaskDto taskDto = new TaskDto(title, details);

        when(todolistRepository.findById(todolistId)).thenReturn(Optional.of(new Todolist()));
        when(taskMapper.convertTaskDtoToTask(taskDto)).thenReturn(new Task(title, details));

        assertDoesNotThrow(() -> taskService.createTask(todolistId, taskDto));

        verify(todolistRepository,times(1)).findById(todolistId);
        verify(taskMapper, times(1)).convertTaskDtoToTask(taskDto);
        verify(taskRepository, times(1)).save(taskMapper.convertTaskDtoToTask(taskDto));
    }

    @Test
    void handleException_createTaskTest() {
        String title = "titleTasks";
        String details = "detailTask";
        UUID todolistId = UUID.randomUUID();

        when(todolistRepository.findById(todolistId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.createTask(todolistId, any(TaskDto.class)));

        verify(todolistRepository,times(1)).findById(todolistId);
        verify(taskMapper, times(0)).convertTaskDtoToTask(any(TaskDto.class));
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void findAllTasksByTodolistIdTest() {
        AppUserDetails user = new AppUserDetails(new AppUser());
        UUID todolistId = UUID.randomUUID();

        when(taskRepository.findAllByUser(user.getAppUser())).thenReturn(new ArrayList<>());
        when(userService.getAuthUser()).thenReturn(user);

        assertDoesNotThrow(() -> taskService.findAllTasksByTodolistId(todolistId));

        verify(taskRepository, times(1)).findAllByUser(user.getAppUser());
    }

    @Test
    void findTaskByIdTest() {
        UUID taskId = UUID.randomUUID();
        Task task =new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.convertTaskToTaskDto(task)).thenReturn(new TaskDto());
        taskService.findTaskById(taskId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, times(1)).convertTaskToTaskDto(task);
    }

    @Test
    void handleException_findTaskByIdTest() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.findTaskById(taskId));

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, times(0)).convertTaskToTaskDto(any(Task.class));
    }


    @Test
    void findTodolistIdByTaskIdTest() {
        UUID taskId = UUID.randomUUID();
        UUID todolistId = UUID.randomUUID();
        Task task =new Task();
        task.setTodolist(new Todolist(todolistId,"title",new AppUser()));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        UUID result =  taskService.findTodolistIdByTaskId(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        assertTrue(result.equals(todolistId));
    }

    @Test
    void handleException_findTodolistIdByTaskIdTest() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.findTodolistIdByTaskId(taskId));

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void completeTaskTest() {
        UUID taskId = UUID.randomUUID();
        boolean isCompleted = true;

        taskService.completeTask(taskId,isCompleted);

        verify(taskRepository, times(1)).updateComplete(taskId, isCompleted);
    }
}