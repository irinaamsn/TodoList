package fintech.todolist.api.controllers;

import fintech.todolist.api.dto.StatisticDto;
import fintech.todolist.api.services.UserService;
import fintech.todolist.client.RestStatisticAPIClient;
import fintech.todolist.details.AppUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
@Tag(name = "Statistic", description = "Controller that performs operations on getting statistic")
@Controller
@RequiredArgsConstructor
@RequestMapping(value="/api/statistic/info/{period}")
public class StatisticApiController {
    private final RestStatisticAPIClient statisticAPIClient;
    private final UserService userService;

    @Operation(summary = "Getting page with statistic")
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "404", description = "Invalid data statisticApiResponse")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public String getStatisticInfoAuthUser(@PathVariable(name="period") String period, Model model){
        UUID userId = userService.getAuthUser().getAppUser().getId();
        Optional<StatisticDto> response=  statisticAPIClient.getStatisticUserByUserId(userId, period);
            model.addAttribute("statistic", response.get());
        Map<String, Integer> graphData = new TreeMap<>();
        graphData.put("до дедлайна", response.get().getCountCompletedTasksBeforeDeadline());
        graphData.put("после дедлайна", response.get().getCountCompletedTasksAfterDeadline());
        model.addAttribute("chartData", graphData);
        return "statistic/index";
    }
}
