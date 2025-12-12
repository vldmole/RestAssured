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

    static UserDto newUserDto() {

        return new UserDto(
                null,
                faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.phoneNumber().phoneNumber(),
                faker.internet().password(),
                null
        );
    }

    @Test
    @Order(1)
    public void createUser_shouldReturn200() {
        request
            .given()
                .body(newUserDto())
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

    static UserDto[] newUserDtoArray(int size){

        UserDto[] v = new UserDto[size];
        for (int i = 0; i < size; i++) {
            v[i] = newUserDto();
        }
        return v;
    }

    @Test
    @Order(2)
    public void createUser_withArray_shouldReturn200() {
        request
            .given()
                .body(newUserDtoArray(5))
            .when()
                .post("/user/createWithArray")
            .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", isA(String.class))
                .body("message", equalTo("ok"))
                .body("type", equalTo("unknown"));
    }

    @Test
    @Order(3)
    public void getUserByUserName_shouldReturn200() {

        var userDto = newUserDto();

        request
            .given()
                .body(userDto)
            .when()
                .post("/user")
            .then()
                .statusCode(200);

            given()
                .basePath("/user")
            .when()
                .get(userDto.username())
            .then()
                .assertThat()
                .statusCode(200)
                .body("firstName", equalTo(userDto.firstName()))
                .body("lastName", equalTo(userDto.lastName()))
                .body("email", equalTo(userDto.email()))
                .body("password", equalTo(userDto.password()))
                .body("phone", equalTo(userDto.phone()))
                .body("id", isA(Long.class))
                .body("id", not(nullValue()));

           given()
               .basePath("/user")
           .when()
               .delete(userDto.username())
           .then()
               .statusCode(200);
    }
}
