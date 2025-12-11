package api.user;


import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import user.dto.UserDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    static RequestSpecification request;

    @BeforeEach
    void setUpRequest() {
        request = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("api-key", "special-key");
    }

    static Faker faker = new Faker();

    UserDto newUser() {

        return new UserDto(
                faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.phoneNumber().phoneNumber(),
                faker.internet().password()
        );
    }

    @Test
    public void createUser_shouldReturn200() {
        request
                .body(newUser())
                .when()
                .post("/user")
                .then()
                .assertThat()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", isA(String.class))
                .body("message", hasLength(19))
                .body("size()", equalTo(3));
    }
}
