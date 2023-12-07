package fintech.todolist.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class StatisticDto {
    private String namePeriod;
    private String avgTimeCompleteTask;
    private int countMadeTask;
    private List<TodolistDto> todolistDtoLists;
    private int countCompletedTasksBeforeDeadline;
    private int countCompletedTasksAfterDeadline;
    private int countCompletedTask;
    private int countPastTask;

}
