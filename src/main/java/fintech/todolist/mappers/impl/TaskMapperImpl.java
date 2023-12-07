package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.models.Task;
import fintech.todolist.exceptions.MapperCovertException;
import fintech.todolist.mappers.TaskMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public Task convertTaskDtoToTask(TaskDto taskDto) {
        if ( taskDto == null ) {
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data task", System.currentTimeMillis());
        }
        Task task = new Task();
        task.setDateTimeCreated(taskDto.getDateTimeCreated());
        task.setTitle( taskDto.getTitle() );
        task.setDetails( taskDto.getDetails() );
        task.setDateTimeLastChange( taskDto.getDateTimeLastChange() );
        task.setCompleted( taskDto.isCompleted() );
        task.setDeadline(taskDto.getDeadline());
        return task;
    }

    @Override
    public TaskDto convertTaskToTaskDto(Task task) {
        if ( task == null ) {
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data task", System.currentTimeMillis());
        }

        TaskDto taskDto = new TaskDto();
        taskDto.setDateTimeCreated(task.getDateTimeCreated());
        taskDto.setId(task.getId());
        taskDto.setTitle( task.getTitle() );
        taskDto.setDetails( task.getDetails() );
        taskDto.setDateTimeLastChange( task.getDateTimeLastChange() );
        taskDto.setCompleted( task.isCompleted() );
        taskDto.setDeadline(task.getDeadline());
        return taskDto;
    }
}
