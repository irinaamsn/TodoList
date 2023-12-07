package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.models.Task;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.api.repositories.TaskRepository;
import fintech.todolist.api.repositories.TodolistRepository;
import fintech.todolist.api.services.TaskService;
import fintech.todolist.api.services.UserService;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.exceptions.NotFoundException;
import fintech.todolist.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final TodolistRepository todolistRepository;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public void deleteTask(UUID taskId) {
        if (!taskRepository.existsById(taskId))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Task not found", System.currentTimeMillis());
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional
    public void updateTask(UUID taskId, TaskDto taskDto) {
        if (!taskRepository.existsById(taskId))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Task not found", System.currentTimeMillis());
        taskRepository.updateById(taskId, taskDto.getTitle(), taskDto.getDetails(), taskDto.getDeadline());
    }

    @Override
    public void createTask(UUID todoListId, TaskDto newTaskDto) {
        Todolist todoList = todolistRepository.findById(todoListId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "TodoList not found", System.currentTimeMillis()));
        Task newTask = taskMapper.convertTaskDtoToTask(newTaskDto);
        newTask.setDateTimeCreated(LocalDateTime.now());
        newTask.setDateTimeLastChange(LocalDateTime.now());
        newTask.setTodolist(todoList);
        newTask.setUser(todoList.getUser());
        taskRepository.save(newTask);
    }

    @Override
    public List<TaskDto> findAllTasksByTodolistId(UUID todolistId) {
        return taskRepository.findAllByUser(userService.getAuthUser().getAppUser()).stream()
                .filter(t -> t.getTodolist().getId()==todolistId)
                .map(taskMapper::convertTaskToTaskDto).collect(Collectors.toList());

    }

    @Override
    public TaskDto findTaskById(UUID taskId) {
        return taskRepository.findById(taskId).map(taskMapper::convertTaskToTaskDto)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Task not found", System.currentTimeMillis()));
    }

    @Override
    public UUID findTodolistIdByTaskId(UUID taskId) {
        Task task= taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Task not found", System.currentTimeMillis()));
        return task.getTodolist().getId();
    }

    @Transactional
    @Override
    public void completeTask(UUID taskId, boolean isCompleted) {
        taskRepository.updateComplete(taskId, isCompleted);
    }
}
