package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.exceptions.MapperCovertException;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {
    @InjectMocks
    private UserMapperImpl userMapper;

    @Test
    void convertToUser() {
        AppUserDto userDto = new AppUserDto("test@example.com", "username", "password");

        AppUser result = userMapper.convertToUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getUsername(), result.getUsername());
        assertEquals(userDto.getPassword(), result.getPassword());
    }
    @Test
    void handleException_NullUserDto_convertToUser() {
        AppUserDto userDto = null;
        assertThrows(MapperCovertException.class, () -> {userMapper.convertToUser(userDto);});
    }
}