package fintech.todolist.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.services.TaskService;
import fintech.todolist.api.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private UserService userService;

    @Test
    void loginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    @Test
    void registrationPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/registration"));
    }

    @Test
    void registration() throws Exception {
        String username ="userTest";
        String password= "123445@User";
        String email = "user@mail.ru";
        AppUserDto appUserDto =new AppUserDto(email,username,password);

        mockMvc.perform(post("/registration")
                        .content(mapper.writeValueAsString(appUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        ArgumentCaptor<AppUserDto> captorUserDto = ArgumentCaptor.forClass(AppUserDto.class);
        verify(userService, times(1)).createUser(captorUserDto.capture());

        AppUserDto capturedUserDto = captorUserDto.getValue();

        assertEquals(appUserDto.getUsername(), capturedUserDto.getUsername());
        assertEquals(appUserDto.getEmail(), capturedUserDto.getEmail());
        assertEquals(appUserDto.getPassword(), capturedUserDto.getPassword());
    }
}