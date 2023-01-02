package space.lingu.lamp.web.data.dto.user;

/**
 * @author RollW
 */
public record UserRegisterRequest(
        String username,
        String password,
        String email) {
}
