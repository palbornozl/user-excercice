package cl.exercise.user.security;

public class JWTConstants {
    public static final String SIGN_UP_URL = "/user/sign-up";
    public static final String SIGN_IN_URL = "/user/sign-in";
    public static final String USER_IN_URL = "/user/in/**";
    public static final long EXPIRATION_TIME = 900_000;
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    JWTConstants() {
    }
}
