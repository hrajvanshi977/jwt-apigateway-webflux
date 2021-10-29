package com.jwtgateway.project.configuration.security.auth;

import com.jwtgateway.project.configuration.security.support.JwtVerifyHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CurrentUserAuthenticationBearer {

    @Value("${jwt.secret}")
    private static String secret;

    private static Logger LOG = LoggerFactory.getLogger(CurrentUserAuthenticationBearer.class);

    public static Mono<Authentication> create(JwtVerifyHandler.VerificationResult verificationResult) {
        var claims = verificationResult.claims;
        var subject = claims.getSubject();

//        HashMap<String, String> existingToken = SessionCache.cache;
//        String username = getUsernameFromToken(verificationResult.token);
//        LOG.info("username"+ "**" + existingToken.get(subject) + "<-->" + verificationResult.token);
//        if (!existingToken.get(subject).equals(verificationResult.token)) {
//            throw new RuntimeException("Token expired");
//        }
        List<String> roles = claims.get("role", List.class);
        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        var principalId = 0L;

        try {
            principalId = Long.parseLong(subject);
        } catch (NumberFormatException ignore) {
        }

        if (principalId == 0)
            return Mono.empty(); // invalid value for any of jwt auth parts

        var principal = new UserPrincipal(principalId, claims.getIssuer());

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }

    private static String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}