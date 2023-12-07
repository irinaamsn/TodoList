package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.StatisticApiResponse;
import fintech.todolist.api.dto.StatisticDto;
import fintech.todolist.exceptions.MapperCovertException;
import fintech.todolist.mappers.StatisticMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class StatisticMapperImpl implements StatisticMapper {
    @Override
    public StatisticDto toStatisticDtoFromApi(StatisticApiResponse statisticApiResponse) {
        if (statisticApiResponse == null)
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data statisticApiResponse", System.currentTimeMillis());
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setCountCompletedTasksAfterDeadline(statisticApiResponse.getCountCompletedTasksAfterDeadline());
        statisticDto.setCountCompletedTask(statisticApiResponse.getCountCompletedTask());
        statisticDto.setCountPastTask(statisticApiResponse.getCountPastTask());
        statisticDto.setTodolistDtoLists(statisticApiResponse.getTodolistDtoLists());
        statisticDto.setAvgTimeCompleteTask(statisticApiResponse.getAvgTimeCompleteTask());
        statisticDto.setCountMadeTask(statisticApiResponse.getCountMadeTask());
        statisticDto.setCountCompletedTasksBeforeDeadline(statisticApiResponse.getCountCompletedTasksBeforeDeadline());
        return statisticDto;
    }
}
