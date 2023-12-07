package fintech.todolist.api.controllers;

import fintech.todolist.api.dto.AppUserDto;
import fintech.todolist.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Auth", description = "Controller that performs operations on login/registration")
@Controller
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final UserService userService;
    @Operation(summary = "Getting page for login")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/login")
    public String loginPage() {
        return "users/login";
    }

    @Operation(summary = "Getting page for registration")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") AppUserDto userDto) {
        return "users/registration";
    }

    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "The request was successfully executed")
    @ApiResponse(responseCode = "400", description = "Username already exists")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody AppUserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
