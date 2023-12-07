package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Role;
import fintech.todolist.api.repositories.RoleRepository;
import fintech.todolist.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void getRole() {
        String roleName="ROLE_USER";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(new Role(roleName)));

        Role result = roleService.getRole(roleName);

        verify(roleRepository, times(1)).findByName(roleName);
        assertEquals(result.getName(), roleName);
    }

    @Test
    void handleException_getRole() {
        String roleName="ROLE_USER";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> roleService.getRole(roleName));

        verify(roleRepository, times(1)).findByName(roleName);
    }
}