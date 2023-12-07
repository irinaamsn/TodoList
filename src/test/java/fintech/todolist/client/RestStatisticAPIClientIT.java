package fintech.todolist.client;

import fintech.todolist.api.dto.StatisticApiResponse;
import fintech.todolist.api.services.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestStatisticAPIClientIT {
    @Autowired
    private UserService userService;
    @Value("${remote.host}")
    private String remoteHost;
    @Value("${remote.port}")
    private String remotePost;
    @Value("${remote.endpoint}")
    private String remoteEndpoint;
    private final String baseUrl = "http://";

    @Test
    void getStatisticUserByUserId() {
        var user= userService.findUserByUsername("admin");
        String testPeriod = "month";
        UriComponents path = UriComponentsBuilder.fromHttpUrl(baseUrl + remoteHost + ":" + remotePost + remoteEndpoint + "/" + user.getId() + "/" + testPeriod).build();
        given()
                .header("Authorization", "admin")
                .header("X-User-Roles", "ROLE_ADMIN")
                .when()
                .get(path.toUriString())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(StatisticApiResponse.class);
    }
}