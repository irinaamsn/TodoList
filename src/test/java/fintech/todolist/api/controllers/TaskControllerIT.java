package fintech.todolist.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.services.TaskService;
import fintech.todolist.api.services.TodolistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    @MockBean
    private TodolistService todolistService;

    private static final ObjectMapper mapper = new ObjectMapper();

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
    void testGetAllTaskByTodolistId() throws Exception {
        UUID todolistId = UUID.randomUUID();

        when(todolistService.findTodolistById(todolistId)).thenReturn(new TodolistDto());
        when(taskService.findAllTasksByTodolistId(todolistId)).thenReturn(List.of(new TaskDto()));

        mockMvc.perform(get("/api/task/{id}", todolistId))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/index"))
                .andExpect(model().attributeExists("todolist"))
                .andExpect(model().attributeExists("tasks"));

        verify(taskService, times(1)).findAllTasksByTodolistId(todolistId);
        verify(todolistService, times(1)).findTodolistById(todolistId);
    }

    @Test
    @WithMockUser(username = "user")
    void getDescribeTaskById() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskDto mockTaskDto = new TaskDto();

        when(taskService.findTaskById(taskId)).thenReturn(mockTaskDto);

        mockMvc.perform(get("/api/task/{id}/show", taskId))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/show"))
                .andExpect(model().attribute("task", mockTaskDto));

        verify(taskService, times(1)).findTaskById(taskId);
    }

    @Test
    @WithMockUser(username = "user")
    void getCreateTaskPage() throws Exception {
        UUID todoListId = UUID.randomUUID();
        TodolistDto mockTodoListDto = new TodolistDto();

        when(todolistService.findTodolistById(todoListId)).thenReturn(mockTodoListDto);

        mockMvc.perform(get("/api/task/{id}/new", todoListId))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().attribute("todolist", mockTodoListDto))
                .andExpect(model().attributeExists("task"));

        verify(todolistService, times(1)).findTodolistById(todoListId);
    }

    @Test
    @WithMockUser(username = "user")
    void createTask() throws Exception {
        UUID todolistId = UUID.randomUUID();
        String title = "title";

        TaskDto mockTaskDto = new TaskDto();
        mockTaskDto.setTitle(title);

        mockMvc.perform(post("/api/task/{id}", todolistId)
                        .content(mapper.writeValueAsString(mockTaskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().isCreated());

        ArgumentCaptor<UUID> captorTodolistId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<TaskDto> captorTaskDto = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskService, times(1)).createTask(captorTodolistId.capture(), captorTaskDto.capture());

        UUID capturedTodolistId = captorTodolistId.getValue();
        TaskDto capturedTaskDto = captorTaskDto.getValue();

        assertEquals(todolistId, capturedTodolistId);
        assertEquals(mockTaskDto.getTitle(), capturedTaskDto.getTitle());
    }

    @Test
    @WithMockUser(username = "user")
    void updateTask() throws Exception {
        UUID taskId = UUID.randomUUID();
        String title = "title";

        TaskDto mockTaskDto = new TaskDto();
        mockTaskDto.setTitle(title);

        mockMvc.perform(put("/api/task/{id}", taskId)
                        .content(mapper.writeValueAsString(mockTaskDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().isOk());

        ArgumentCaptor<UUID> captorTodolistId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<TaskDto> captorTaskDto = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskService, times(1)).updateTask(captorTodolistId.capture(), captorTaskDto.capture());

        UUID capturedTodolistId = captorTodolistId.getValue();
        TaskDto capturedTaskDto = captorTaskDto.getValue();

        assertEquals(taskId, capturedTodolistId);
        assertEquals(mockTaskDto.getTitle(), capturedTaskDto.getTitle());
    }

    @Test
    @WithMockUser(username = "user")
    void edit() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskDto mockTaskDto = new TaskDto();

        when(taskService.findTaskById(taskId)).thenReturn(mockTaskDto);

        mockMvc.perform(get("/api/task/{id}/edit", taskId))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"))
                .andExpect(model().attribute("task", mockTaskDto));

        verify(taskService, times(1)).findTaskById(taskId);
    }

    @Test
    @WithMockUser(username = "user")
    void deleteTask() throws Exception {
        UUID taskId = UUID.randomUUID();

        mockMvc.perform(delete("/api/task/{id}", taskId))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<UUID> captorTodolistId = ArgumentCaptor.forClass(UUID.class);
        verify(taskService, times(1)).deleteTask(captorTodolistId.capture());

        UUID capturedTodolistId = captorTodolistId.getValue();

        assertEquals(taskId, capturedTodolistId);
    }

    @Test
    @WithMockUser(username = "user")
    void updateCompleteTask() throws Exception {
        UUID taskId = UUID.randomUUID();

        mockMvc.perform(put("/api/task/complete/{id}", taskId))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<UUID> capturedTaskId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Boolean> captorComplete = ArgumentCaptor.forClass(Boolean.class);
        verify(taskService, times(1)).completeTask(capturedTaskId.capture(),captorComplete.capture());

        Boolean capturedComplete = captorComplete.getValue();

        assertEquals(taskId, capturedTaskId.getValue());
        assertTrue(capturedComplete);
    }
}