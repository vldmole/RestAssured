package user.dto;

public record UserDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        Integer userStatus
) {
    //nothing for while
}
