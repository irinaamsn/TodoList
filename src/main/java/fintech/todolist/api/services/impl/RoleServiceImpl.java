package fintech.todolist.api.services.impl;

import fintech.todolist.api.models.Role;
import fintech.todolist.api.repositories.RoleRepository;
import fintech.todolist.api.services.RoleService;
import fintech.todolist.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Role not found", System.currentTimeMillis()));
    }
}
