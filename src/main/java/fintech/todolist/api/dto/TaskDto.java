package fintech.todolist.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDto {
    private UUID id;
    @Schema(description = "Task title")
    private String title;
    @Schema(description = "Task description")
    private String details;
    @Schema(description = "Task deadline")
    private LocalDateTime deadline;
    @Schema(description = "Task completion time")
    private LocalDateTime dateTimeLastChange;
    @Schema(description = "The time when the task was created")
    private LocalDateTime dateTimeCreated;
    @Schema(description = "Task readiness status")
    private boolean isCompleted;

    public TaskDto(String title, String details) {
        this.title = title;
        this.details = details;
    }
    public TaskDto(String title, String details, LocalDateTime dateTimeLastChange) {
        this.title = title;
        this.details = details;
        this.dateTimeLastChange=dateTimeLastChange;
    }
}
