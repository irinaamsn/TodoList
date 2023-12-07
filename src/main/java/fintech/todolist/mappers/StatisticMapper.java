package fintech.todolist.mappers;

import fintech.todolist.api.dto.StatisticApiResponse;
import fintech.todolist.api.dto.StatisticDto;

public interface StatisticMapper {
    StatisticDto toStatisticDtoFromApi(StatisticApiResponse statisticApiResponse);
}
