package fintech.todolist.config;

import fintech.todolist.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(a -> {
                    a.requestMatchers(antMatcher("/swagger-ui/**")).hasRole("ADMIN");
                    a.requestMatchers(antMatcher("/swagger-ui.html")).hasRole("ADMIN");
                    a.requestMatchers(antMatcher("/v3/api-docs/**")).hasRole("ADMIN");
                    a.requestMatchers(antMatcher("/api/todolist/**")).hasAnyRole("USER","ADMIN");
                    a.requestMatchers(antMatcher("/api/task/**")).hasAnyRole("USER","ADMIN");
                    a.requestMatchers(antMatcher("/api/statistic/info/**")).hasAnyRole("USER","ADMIN");
                    a.requestMatchers("/registration","/login", "/error").permitAll();
                })
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(fl->fl.loginPage("/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/api/todolist",true))
                .logout(l->l.logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));

        return http.build();
    }
}
