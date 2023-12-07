package fintech.todolist.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticApiResponse {
    @JsonProperty(value = "todolistDtoLists")
    private List<TodolistDto> todolistDtoLists;
    @JsonProperty(value = "avgTimeCompleteTask")
    private String avgTimeCompleteTask;
    @JsonProperty(value = "countMadeTask")
    private int countMadeTask;
    @JsonProperty(value = "countCompletedTasksBeforeDeadline")
    private int countCompletedTasksBeforeDeadline;
    @JsonProperty(value = "countCompletedTasksAfterDeadline")
    private int countCompletedTasksAfterDeadline;
    @JsonProperty(value = "countCompletedTask")
    private int countCompletedTask;
    @JsonProperty(value = "countPastTask")
    private int countPastTask;
}
