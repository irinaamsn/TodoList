package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Role;
import fintech.todolist.api.models.Todolist;
import fintech.todolist.api.models.enums.RoleEnum;
import fintech.todolist.api.repositories.RoleRepository;
import fintech.todolist.api.repositories.UserRepository;
import fintech.todolist.api.services.RoleService;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.exceptions.NotCreatedException;
import fintech.todolist.exceptions.NotFoundException;
import fintech.todolist.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        String roleName = "ROLE_USER";
        AppUserDto userDto = new AppUserDto("email", "user", "password");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userMapper.convertToUser(userDto)).thenReturn(new AppUser(userDto.getEmail(), userDto.getUsername(), userDto.getPassword()));
        when(roleService.getRole(roleName)).thenReturn(new Role(roleName));

        assertDoesNotThrow(() -> userService.createUser(userDto));

        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userMapper, times(1)).convertToUser(userDto);
        verify(roleService, times(1)).getRole(roleName);
        verify(userRepository, times(1)).save(userMapper.convertToUser(userDto));
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
    }

    @Test
    void handleException_createUser() {
        String roleName = "ROLE_USER";
        AppUserDto userDto = new AppUserDto("email", "user", "password");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        assertThrows(NotCreatedException.class, () -> userService.createUser(userDto));

        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userMapper, never()).convertToUser(userDto);
        verify(roleService, never()).getRole(roleName);
        verify(userRepository, never()).save(userMapper.convertToUser(userDto));
        verify(passwordEncoder, never()).encode(userDto.getPassword());
    }

    @Test
    void findUserByUsername() {
        String login = "user";
        AppUser user = new AppUser();
        user.setUsername(login);

        when(userRepository.findByUsername(login)).thenReturn(Optional.of(user));

        AppUser result = userService.findUserByUsername(login);

        verify(userRepository, times(1)).findByUsername(login);
        assertEquals(result.getUsername(), login);
    }

    @Test
    void handleException_findUserByUsername() {
        String login = "user";
        AppUser user = new AppUser();
        user.setUsername(login);

        when(userRepository.findByUsername(login)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserByUsername(login));

        verify(userRepository, times(1)).findByUsername(login);
    }

    @Test
    void loadUserByUsername() {
        String login = "user";
        AppUser user = new AppUser();
        user.setUsername(login);

        when(userRepository.findByUsername(login)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(login);

        verify(userRepository, times(1)).findByUsername(login);
        assertEquals(result.getUsername(), login);
    }

    @Test
    void handleException_loadUserByUsername() {
        String login = "user";
        AppUser user = new AppUser();
        user.setUsername(login);

        when(userRepository.findByUsername(login)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loadUserByUsername(login));

        verify(userRepository, times(1)).findByUsername(login);
    }

    @Test
    public void testGetAuthUser() {
        String testUsername = "testUser";
        AppUserDetails fakeUserDetails = new AppUserDetails(new AppUser(testUsername));
        Authentication fakeAuthentication = mock(Authentication.class);

        when(fakeAuthentication.getPrincipal()).thenReturn(fakeUserDetails);

        SecurityContextHolder.getContext().setAuthentication(fakeAuthentication);

        AppUserDetails result = userService.getAuthUser();

        assertEquals(testUsername, result.getUsername());
    }
}