package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.StatisticApiResponse;
import fintech.todolist.api.dto.StatisticDto;
import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.exceptions.MapperCovertException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class StatisticMapperImplTest {
    @InjectMocks
    private StatisticMapperImpl statisticMapper;

    @Test
    void toStatisticDtoFromApiTest() {
        StatisticApiResponse apiResponse = new StatisticApiResponse();
        apiResponse.setTodolistDtoLists(List.of(new TodolistDto()));
        apiResponse.setAvgTimeCompleteTask("1 день 0 часов 0 минут");
        apiResponse.setCountMadeTask(4);
        apiResponse.setCountCompletedTasksBeforeDeadline(2);

        StatisticDto result = statisticMapper.toStatisticDtoFromApi(apiResponse);

        assertNotNull(result);
        assertEquals(apiResponse.getTodolistDtoLists(), result.getTodolistDtoLists());
        assertEquals(apiResponse.getAvgTimeCompleteTask(), result.getAvgTimeCompleteTask());
        assertEquals(apiResponse.getCountMadeTask(), result.getCountMadeTask());
        assertEquals(apiResponse.getCountCompletedTasksBeforeDeadline(), result.getCountCompletedTasksBeforeDeadline());
    }
    @Test
    void handleException_NullApiResponse_toStatisticDtoFromApiTest() {
        StatisticApiResponse apiResponse = null;
        assertThrows(MapperCovertException.class, () -> {statisticMapper.toStatisticDtoFromApi(apiResponse);});
    }
}