package fintech.todolist.mappers;


import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.models.AppUser;

public interface UserMapper {
    AppUser convertToUser(AppUserDto userDto);
}
