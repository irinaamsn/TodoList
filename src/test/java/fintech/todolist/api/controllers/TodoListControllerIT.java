package fintech.todolist.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.services.TodolistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TodoListControllerIT {
    @Autowired
    private MockMvc mockMvc;
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
    void getNewPage() throws Exception {
        mockMvc.perform(get("/api/todolist/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("todolists/new"))
                .andExpect(model().attributeExists("todolist"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getTodolistPage() throws Exception {
        List<TodolistDto> list = List.of(new TodolistDto());

        when(todolistService.getAllTodolistDtoByUser()).thenReturn(list);

        mockMvc.perform(get("/api/todolist"))
                .andExpect(status().isOk())
                .andExpect(view().name("todolists/index"))
                .andExpect(model().attribute("todolists", list))
                .andExpect(model().attributeExists("username"));

        verify(todolistService, times(1)).getAllTodolistDtoByUser();
    }

    @Test
    @WithMockUser(username = "user")
    void createTodolist() throws Exception {
        TodolistDto mockTodolistDto = new TodolistDto();
        mockTodolistDto.setTitle("title");

        mockMvc.perform(post("/api/todolist")
                        .content(mapper.writeValueAsString(mockTodolistDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().isCreated());

        ArgumentCaptor<TodolistDto> captorTodolistDto = ArgumentCaptor.forClass(TodolistDto.class);
        verify(todolistService, times(1)).createTodolist(captorTodolistDto.capture());

        TodolistDto capturedTodolistDto = captorTodolistDto.getValue();

        assertEquals(mockTodolistDto.getTitle(), capturedTodolistDto.getTitle());
    }

    @Test
    @WithMockUser(username = "user")
    void edit() throws Exception {
        UUID todolistId = UUID.randomUUID();
        TodolistDto mockTodolistDto = new TodolistDto();

        when(todolistService.findTodolistById(todolistId)).thenReturn(mockTodolistDto);

        mockMvc.perform(get("/api/todolist/{id}/edit", todolistId))
                .andExpect(status().isOk())
                .andExpect(view().name("todolists/edit"))
                .andExpect(model().attribute("todolist", mockTodolistDto));

        verify(todolistService, times(1)).findTodolistById(todolistId);
    }

    @Test
    @WithMockUser(username = "user")
    void updateTodolist() throws Exception {
        UUID todolistId = UUID.randomUUID();
        String title = "title";

        TodolistDto mockTodolistDto = new TodolistDto(todolistId, title);

        mockMvc.perform(put("/api/todolist/{id}", todolistId)
                        .content(mapper.writeValueAsString(mockTodolistDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().isOk());

        ArgumentCaptor<UUID> captorTodolistId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<TodolistDto> captorTodolistDto = ArgumentCaptor.forClass(TodolistDto.class);
        verify(todolistService, times(1)).updateTodolist(captorTodolistId.capture(), captorTodolistDto.capture());

        UUID capturedTodolistId = captorTodolistId.getValue();
        TodolistDto capturedTodolistDto = captorTodolistDto.getValue();

        assertEquals(todolistId, capturedTodolistId);
        assertEquals(mockTodolistDto.getTitle(), capturedTodolistDto.getTitle());
    }

    @Test
    @WithMockUser(username = "user")
    void deleteTodolist() throws Exception {
        UUID todolistId = UUID.randomUUID();

        mockMvc.perform(delete("/api/todolist/{id}", todolistId))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<UUID> captorTodolistId = ArgumentCaptor.forClass(UUID.class);
        verify(todolistService, times(1)).deleteTodolist(captorTodolistId.capture());

        UUID capturedTodolistId = captorTodolistId.getValue();

        assertEquals(todolistId, capturedTodolistId);
    }
}