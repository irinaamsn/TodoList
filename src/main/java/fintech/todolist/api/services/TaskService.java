package fintech.todolist.api.services;

import fintech.todolist.api.dto.TaskDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    void deleteTask(UUID id);
    void updateTask(UUID taskId, TaskDto taskDto);
    void createTask(UUID taskId, TaskDto newTaskDto);
    List<TaskDto> findAllTasksByTodolistId(UUID todolistId);
    TaskDto findTaskById(UUID tskId);
    UUID findTodolistIdByTaskId(UUID taskId);
    void completeTask(UUID taskId, boolean isCompleted);
}
