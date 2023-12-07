package fintech.todolist.api.controllers;

import fintech.todolist.api.dto.StatisticDto;
import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.services.UserService;
import fintech.todolist.client.RestStatisticAPIClient;
import fintech.todolist.details.AppUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticApiControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private RestStatisticAPIClient statisticAPIClient;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user")
    void getStatisticInfoAuthUserMonth() throws Exception {
        UUID userId = UUID.randomUUID();
        String username = "user";
        String password = "password";
        String period = "month";
        AppUser appUser = new AppUser(userId, username, password);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);
        StatisticDto mockStatisticDto = new StatisticDto();
        mockStatisticDto.setTodolistDtoLists(List.of(new TodolistDto()));

        when(userService.getAuthUser()).thenReturn(appUserDetails);
        when(statisticAPIClient.getStatisticUserByUserId(userId, period)).thenReturn(Optional.of(mockStatisticDto));

        mockMvc.perform(get("/api/statistic/info/{period}", period))
                .andExpect(status().isOk())
                .andExpect(view().name("statistic/index"))
                .andExpect(model().attribute("statistic", mockStatisticDto));

        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> periodCaptor = ArgumentCaptor.forClass(String.class);

        verify(userService, times(1)).getAuthUser();
        verify(statisticAPIClient, times(1)).getStatisticUserByUserId(userIdCaptor.capture(), periodCaptor.capture());

        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(period, periodCaptor.getValue());
    }

    @Test
    @WithMockUser(username = "user")
    void getStatisticInfoAuthUserWeek() throws Exception {
        UUID userId = UUID.randomUUID();
        String username = "user";
        String password = "password";
        String period = "week";
        AppUser appUser = new AppUser(userId, username, password);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);
        StatisticDto mockStatisticDto = new StatisticDto();
        mockStatisticDto.setTodolistDtoLists(List.of(new TodolistDto()));

        when(userService.getAuthUser()).thenReturn(appUserDetails);
        when(statisticAPIClient.getStatisticUserByUserId(userId, period)).thenReturn(Optional.of(mockStatisticDto));

        mockMvc.perform(get("/api/statistic/info/{period}", period))
                .andExpect(status().isOk())
                .andExpect(view().name("statistic/index"))
                .andExpect(model().attribute("statistic", mockStatisticDto));


        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> periodCaptor = ArgumentCaptor.forClass(String.class);

        verify(userService, times(1)).getAuthUser();
        verify(statisticAPIClient, times(1)).getStatisticUserByUserId(userIdCaptor.capture(), periodCaptor.capture());

        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(period, periodCaptor.getValue());
    }
}