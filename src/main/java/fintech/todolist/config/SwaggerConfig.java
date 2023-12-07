package fintech.todolist.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition(
        info = @Info(
                title = "Todolist Service Api",
                description = "Todolist Service",
                version = "1.0.0"
        )
)
public class SwaggerConfig {

}
