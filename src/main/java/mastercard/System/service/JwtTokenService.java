package mastercard.System.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import mastercard.System.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JwtTokenService {

    Dotenv dotenv = Dotenv.load();

    private final String secret = dotenv.get("JWT_SECRET");
    private final long jwtExpiration = Long.parseLong(dotenv.get("JWT_EXPIRATION_TIME_MS"));
    private final long expirationTimeMillis = System.currentTimeMillis() + jwtExpiration;

    public String generate(User user) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(expirationTimeMillis))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.warn("Error while creating JWT", e);
        }
        return "";
    }

    public String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            log.warn("Error while token verification", e);
        }
        return "";
    }
}
