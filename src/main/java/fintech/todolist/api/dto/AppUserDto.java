package fintech.todolist.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    @Schema(description = "Email")
    @Email(message = "Incorrect email")
    private String email;

    @Schema(description = "Login")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "Incorrect login")
    private String username;

    @Schema(description = "Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Incorrect password")
    private String password;
}
