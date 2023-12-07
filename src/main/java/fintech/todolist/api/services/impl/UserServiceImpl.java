package fintech.todolist.api.services.impl;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Role;
import fintech.todolist.api.models.enums.RoleEnum;
import fintech.todolist.api.repositories.UserRepository;
import fintech.todolist.api.services.RoleService;
import fintech.todolist.api.services.UserService;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.exceptions.NotCreatedException;
import fintech.todolist.exceptions.NotFoundException;
import fintech.todolist.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void createUser(AppUserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST, "Username already exists", System.currentTimeMillis());
        AppUser user = userMapper.convertToUser(userDto);
        Role role = roleService.getRole(RoleEnum.USER.getAuthority());
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found", System.currentTimeMillis()));
    }

    @Override
    public AppUserDetails getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (AppUserDetails) authentication.getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found", System.currentTimeMillis()));
        return new AppUserDetails(user);
    }
}
