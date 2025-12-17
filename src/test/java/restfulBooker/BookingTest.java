package restfulBooker;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import restfulBooker.dto.BookingDto;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingTest {


    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    static RequestSpecification request;

    @BeforeEach
    void setUpRequest() {
        request = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
    }

    static record BookingIdDto(Integer bookingid) { /*nothing*/ }
    static BookingIdDto[] bookingIdDtos = null;
    static int currentIdx = -1;
    static BookingDto currentBookingDto;

    @Test
    @Order(1)
    @DisplayName("Check if the response body contains an array of booking IDs")
    void getAllBookings(){
        Response response = given().when().get("/booking");
        assertEquals(200, response.statusCode());

        bookingIdDtos = response.as(BookingIdDto[].class);
        assertTrue(bookingIdDtos.length>0);
    }

    @Test
    @Order(2)
    @DisplayName("Look for a specific bookingid")
    void GetAllBookingById(){

        currentIdx = ((int)(Math.random()*10_000)) % bookingIdDtos.length;
        Response response = given().when().get("/booking/" + bookingIdDtos[currentIdx].bookingid);
        assertEquals(200, response.statusCode());

        currentBookingDto = response.as(BookingDto.class);
        assertNotNull(currentBookingDto);
    }

    @Test
    @Order(3)
    @DisplayName("Look for bookings filtered by firsName")
    void GetAllBookingFilteringByFirstName(){

        Response response = request.given()
                    .queryParam("firstname", currentBookingDto.firstname())
                .when()
                    .get("/booking");

        assertEquals(200, response.statusCode());

        var recovered = response.as(BookingIdDto[].class);
        assertTrue(recovered.length > 0);

        var id = recovered[(int)(Math.random()*recovered.length)].bookingid;
        Response resp = request.given().when().get("/booking/" + id);
        assertEquals(200, resp.statusCode());

        var bookingDto = resp.as(BookingDto.class);
        assertNotNull(bookingDto);
        assertEquals(currentBookingDto.firstname(), bookingDto.firstname());
    }

    @Test
    @Order(4)
    @DisplayName("Look for bookings filtered by lastName")
    void GetAllBookingFilteringByLastName(){

        Response response = request.given().queryParam("lastname", currentBookingDto.lastname())
                            .when().get("/booking");

        assertEquals(200, response.statusCode());

        var recovered = response.as(BookingIdDto[].class);
        assertTrue(recovered.length > 0);

        var id = recovered[(int)(Math.random()*recovered.length)].bookingid;

        Response resp = request.given().when().get("/booking/" + id);
        assertEquals(200, resp.statusCode());

        var bookingDto = resp.as(BookingDto.class);
        assertNotNull(bookingDto);
        assertEquals(currentBookingDto.lastname(), bookingDto.lastname());
    }

    @Test
    @Order(5)
    @DisplayName("Look for bookings filtered by lastName")
    void GetAllBookingFilteringByFirstNameAndLastName(){

        Response response = request.given()
                    .queryParam("lastname", currentBookingDto.lastname())
                    .queryParam("firstname", currentBookingDto.firstname())
                .when()
                    .get("/booking");

        assertEquals(200, response.statusCode());

        var recovered = response.as(BookingIdDto[].class);
        assertTrue(recovered.length > 0);

        var id = recovered[(int)(Math.random()*recovered.length)].bookingid;

        Response resp = request.given().when().get("/booking/" + id);
        assertEquals(200, resp.statusCode());

        var bookingDto = resp.as(BookingDto.class);
        assertNotNull(bookingDto);
        assertEquals(currentBookingDto.firstname(), bookingDto.firstname());
        assertEquals(currentBookingDto.lastname(), bookingDto.lastname());
    }
}
