package restfulBooker.dto;

import java.time.LocalDate;

record BookingDates(LocalDate checkin, LocalDate checkout) {
    //nothing
}

public record BookingDto(
        String firstname,
        String lastname,
        Integer totalprice,
        Boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds
) {
    //nothing
}
