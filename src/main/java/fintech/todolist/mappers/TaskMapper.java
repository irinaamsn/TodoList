package fintech.todolist.mappers;

import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.models.Task;

public interface TaskMapper {

    Task convertTaskDtoToTask(TaskDto taskDto);
    TaskDto convertTaskToTaskDto(Task taskDto);
}
