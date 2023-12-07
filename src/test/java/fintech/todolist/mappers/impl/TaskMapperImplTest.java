package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.models.Task;
import fintech.todolist.exceptions.MapperCovertException;
import fintech.todolist.mappers.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TaskMapperImplTest {
    @InjectMocks
    private TaskMapperImpl taskMapper;

    @Test
    void convertTaskDtoToTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setDateTimeCreated(LocalDateTime.now().minusDays(3));
        taskDto.setTitle("testTitle");
        taskDto.setDetails("testDetails");
        taskDto.setDateTimeLastChange(LocalDateTime.now().minusDays(1));
        taskDto.setCompleted(true);
        taskDto.setDeadline(LocalDateTime.now().plusDays(2));

        Task result = taskMapper.convertTaskDtoToTask(taskDto);

        assertNotNull(result);
        assertEquals(taskDto.getDateTimeCreated(), result.getDateTimeCreated());
        assertEquals(taskDto.getTitle(), result.getTitle());
        assertEquals(taskDto.getDetails(), result.getDetails());
        assertEquals(taskDto.getDateTimeLastChange(), result.getDateTimeLastChange());
        assertEquals(taskDto.isCompleted(), result.isCompleted());
        assertEquals(taskDto.getDeadline(), result.getDeadline());
    }

    @Test
    void handleException_NullTaskDto_convertTaskDtoToTask() {
        TaskDto taskDto = null;
        assertThrows(MapperCovertException.class, () -> {taskMapper.convertTaskDtoToTask(taskDto);});
    }

    @Test
    void convertTaskToTaskDto() {
        Task task = new Task();
        task.setDateTimeCreated(LocalDateTime.now().minusDays(3));
        task.setId(UUID.randomUUID());
        task.setTitle("testTitle");
        task.setDetails("testDetails");
        task.setDateTimeLastChange(LocalDateTime.now().minusDays(1));
        task.setCompleted(true);
        task.setDeadline(LocalDateTime.now().plusDays(2));

        TaskDto result = taskMapper.convertTaskToTaskDto(task);

        assertNotNull(result);
        assertEquals(task.getDateTimeCreated(), result.getDateTimeCreated());
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDetails(), result.getDetails());
        assertEquals(task.getDateTimeLastChange(), result.getDateTimeLastChange());
        assertEquals(task.isCompleted(), result.isCompleted());
        assertEquals(task.getDeadline(), result.getDeadline());
    }

    @Test
    void handleException_NullTask_convertTaskToTaskDto() {
        Task task = null;
        assertThrows(MapperCovertException.class, () -> {taskMapper.convertTaskToTaskDto(task);});
    }
}