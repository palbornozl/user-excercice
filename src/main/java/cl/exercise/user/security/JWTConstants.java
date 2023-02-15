package cl.exercise.user.security;

public class JWTConstants {
  public static final String SIGN_UP_URL = "/user/sign-up";
  public static final long EXPIRATION_TIME = 900_000;
  public static final String HEADER_STRING = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  JWTConstants() {
  }
}
