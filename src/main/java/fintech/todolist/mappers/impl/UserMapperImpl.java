package fintech.todolist.mappers.impl;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.exceptions.MapperCovertException;
import fintech.todolist.mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public AppUser convertToUser(AppUserDto userDto) {
        if (userDto == null)
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data person", System.currentTimeMillis());
        AppUser user = new AppUser();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
