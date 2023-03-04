package cl.exercise.user.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;

import static cl.exercise.user.security.JWTConstants.EXPIRATION_TIME;
import static cl.exercise.user.security.JWTConstants.TOKEN_PREFIX;

@Component
@Slf4j
@Getter
@Setter
public class TokenUtils {
    private String accessTokenSecret;
    private String accessTokenValiditySecondsStr;
    private Long accessTokenValiditySeconds;

    public TokenUtils(@Value("${vault.my.secret}") String accessTokenSecret,
                      @Value("${vault.my.time.vality}") String accessTokenValiditySecondsStr) {
        this.accessTokenSecret = accessTokenSecret;
        this.accessTokenValiditySecondsStr = accessTokenValiditySecondsStr;
    }

    @SneakyThrows
    public String createToken(UserDetailsImpl userDetails) {
        log.debug("--- accessTokenValiditySeconds");
        convertTimeValityToLong();
        long expirationTime = accessTokenValiditySeconds * 1_000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("userCode", userDetails.getId());
        extra.put("userName", userDetails.getName());
        extra.put("password", userDetails.getPassword());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(accessTokenSecret.getBytes()))
                .compact();
    }

    @SneakyThrows
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(accessTokenSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();

        return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    }

    private void convertTimeValityToLong() {
        try {
            this.accessTokenValiditySeconds = Long.parseLong(accessTokenValiditySecondsStr.replace("_", "").replace("L", ""));
        } catch (NumberFormatException n) {
            this.accessTokenValiditySeconds = EXPIRATION_TIME;
        }
    }

    @SneakyThrows
    public String decodeToken(String token) {
        String[] chunks = token.replace(TOKEN_PREFIX, "").trim().split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

        JsonNode jsonNode = new ObjectMapper().readTree(payload);
        return jsonNode.get("sub").asText();
    }
}
