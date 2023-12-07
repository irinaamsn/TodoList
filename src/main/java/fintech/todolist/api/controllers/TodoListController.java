package fintech.todolist.api.controllers;

import fintech.todolist.api.dto.TodolistDto;
import fintech.todolist.api.services.TodolistService;
import fintech.todolist.details.AppUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Tag(name = "Todolist", description = "Controller that performs operations on todolists")
@Controller
@RequestMapping("/api/todolist")
@RequiredArgsConstructor
public class TodoListController {
    private final TodolistService todoListService;

    @Operation(summary = "Getting page for creating new todolist")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/new")
    public String getNewPage(@ModelAttribute("todolist") TodolistDto todolistDto) {
        return "todolists/new";
    }

    @Operation(summary = "Getting page with all todolist", description = "Getting page with all todolist by auth user")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public String getTodolistPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("todolists",todoListService.getAllTodolistDtoByUser());
        return "todolists/index";
    }

    @Operation(summary = "Create new todolist", description = "Creating a new todolist with data")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public ResponseEntity<?> createTodolist(@RequestBody TodolistDto todolistDto) {
        todoListService.createTodolist(todolistDto);
        return new ResponseEntity<>("Todolist created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Getting page for edit exists todolist", description = "Getting page for edit exists todolist by id")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Todolist not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") @Parameter(description = "todolistId") UUID todolistId,
                       Model model){
        model.addAttribute("todolist",todoListService.findTodolistById(todolistId));
        return "todolists/edit";
    }

    @Operation(summary = "Update exists todolist", description = "Update todolist new data by id")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Todolist not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateTodolist(@PathVariable(name="id") @Parameter(description = "todolistId") UUID todolistId,
                                 @RequestBody TodolistDto todoListDto) {
        todoListService.updateTodolist(todolistId, todoListDto);
        return ResponseEntity.ok("Todolist updated successfully");
    }

    @Operation(summary = "Delete todolist", description = "Delete exists todolist by id")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping(path = "/{id}")
    public String deleteTodolist(@PathVariable(name="id") @Parameter(description = "todolistId") UUID todolistId) {
        todoListService.deleteTodolist(todolistId);
        return "redirect:/api/todolist";
    }
}
