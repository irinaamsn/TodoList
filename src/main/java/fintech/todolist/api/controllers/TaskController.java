package fintech.todolist.api.controllers;

import fintech.todolist.api.dto.TaskDto;
import fintech.todolist.api.services.TaskService;
import fintech.todolist.api.services.TodolistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Task", description = "Controller that performs operations on tasks")
@Controller
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TodolistService todoListService;

    @Operation(summary = "Getting page with tasks", description = "Getting all tasks by ID todolist")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Todolist not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public String getAllTaskByTodolistId(@PathVariable("id") @Parameter(description = "todolistId") UUID todolistId,
                                         Model model) {
        model.addAttribute("todolist", todoListService.findTodolistById(todolistId));
        model.addAttribute("tasks", taskService.findAllTasksByTodolistId(todolistId));
        return "tasks/index";
    }

    @Operation(summary = "Getting page with detailed information about the task", description = "Getting task with detailed information by taskId")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}/show")
    public String getDescribeTaskById(@PathVariable("id") @Parameter(description = "taskId") UUID taskId,
                                      Model model) {
        model.addAttribute("task", taskService.findTaskById(taskId));
        return "tasks/show";
    }

    @Operation(summary = "Getting page for creating a new task", description = "Getting page for creating a new task and todolist of this task")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Todolist not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}/new")
    public String getCreateTaskPage(@PathVariable(name = "id") @Parameter(description = "todolistId") UUID todoListId,
                                    @ModelAttribute("task") TaskDto taskDto,
                                    Model model) {
        model.addAttribute("todolist", todoListService.findTodolistById(todoListId));
        return "tasks/new";
    }

    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Todolist not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/{id}")
    public ResponseEntity<?> createTask(@PathVariable(name = "id") @Parameter(description = "todolistId") UUID todolistId,
                                             @RequestBody TaskDto taskDto) {
        taskService.createTask(todolistId, taskDto);
        return new ResponseEntity<>("Task created successfully",  HttpStatus.CREATED);
    }

    @Operation(summary = "Getting page for edit exists task", description = "Getting page for edit exists task by taskId")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") @Parameter(description = "taskId") UUID taskId,
                       Model model) {
        model.addAttribute("task", taskService.findTaskById(taskId));
        return "tasks/edit";
    }

    @Operation(summary = "Update exists task", description = "Update task new data by taskId")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable(name = "id") @Parameter(description = "taskId") UUID taskId,
                                             @RequestBody TaskDto taskDto) {
        taskService.updateTask(taskId, taskDto);
        return ResponseEntity.ok("Task updated successfully");
    }

    @Operation(summary = "Delete exists task", description = "Delete task by taskId")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable(name = "id") @Parameter(description = "taskId") UUID taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/api/todolist";
    }

    @Operation(summary = "Update exists task", description = "update the task status to completed by taskId")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping("/complete/{id}")
    public String updateCompleteTask(@PathVariable("id") @Parameter(description = "taskId") UUID taskId) {
        taskService.completeTask(taskId, true);
        return "redirect:/api/todolist";
    }
}
