package fintech.todolist.api.services;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.details.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    void createUser(AppUserDto userDto);
    AppUser findUserByUsername(String username);
    AppUserDetails getAuthUser();
}
