package user.dto;

public record UserDto(
        String username,
        String firstname,
        String lastname,
        String email,
        String phone,
        String password
) {
    //nothing for while
}
