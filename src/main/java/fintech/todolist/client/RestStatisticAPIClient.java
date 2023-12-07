package fintech.todolist.client;

import fintech.todolist.api.dto.StatisticApiResponse;
import fintech.todolist.api.dto.StatisticDto;
import fintech.todolist.api.services.UserService;
import fintech.todolist.details.AppUserDetails;
import fintech.todolist.mappers.StatisticMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestStatisticAPIClient {
    private final RestTemplate restTemplate;
    private final StatisticMapper statisticMapper;
    private final UserService userService;
    @Value("${remote.host}")
    private String remoteHost;
    @Value("${remote.port}")
    private String remotePost;
    @Value("${remote.endpoint}")
    private String remoteEndpoint;
    private final String baseUrl = "http://";

    public RestStatisticAPIClient(@Qualifier("statisticApiRestTemplate") RestTemplate restTemplate, StatisticMapper statisticMapper, UserService userService) {
        this.restTemplate = restTemplate;
        this.statisticMapper = statisticMapper;
        this.userService = userService;
    }

    @RateLimiter(name = "userRateLimiter")
    public Optional<StatisticDto> getStatisticUserByUserId(UUID userId, String period) {
        ResponseEntity<StatisticApiResponse> response = restTemplate.exchange(
                createUrl(userId, period),
                HttpMethod.GET,
                createEntity(userService.getAuthUser()),
                StatisticApiResponse.class
        );
        StatisticDto statisticDto = statisticMapper.toStatisticDtoFromApi(response.getBody());
        statisticDto.setNamePeriod(period);
        return Optional.ofNullable(statisticDto);
    }

    private String createUrl(UUID userId, String period) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseUrl + remoteHost + ":" + remotePost + remoteEndpoint + "/" + userId + "/" + period).build();
        return builder.toUriString();
    }

    private HttpEntity<String> createEntity(AppUserDetails authUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization_h", authUser.getUsername());
        headers.set("X-User-Roles", authUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));
        return new HttpEntity<>(headers);
    }
}
