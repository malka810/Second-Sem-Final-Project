package lk.ijse.online_course_management.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lk.ijse.online_course_management.dto.UserDTO;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@PropertySource(ignoreResourceNotFound = true,value = "classpath:otherprops.properties")
public class JwtUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = 234234523523L;

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 12;
    private final UserRepo userRepo;

    @Value("2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566DF423F4428472B4B6250655368566D")
    private String secretKey;

    public JwtUtil(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Claims getUserRoleCodeFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userDTO.getRole());
        return doGenerateToken(claims, userDTO.getEmail());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

//    public UUID getUserIdFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String subject = claims.getSubject();
//        try {
//            return UUID.fromString(subject);
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Token subject is not a valid UUID: " + subject);
//        }
//    }
//public UUID getUserIdFromToken(String token) {
//    String subject = getSubjectFromToken(token);
//
//    // First try to parse as UUID
//    try {
//        return UUID.fromString(subject);
//    } catch (IllegalArgumentException e) {
//        // If not UUID, treat as email and look up user
//        User user = userRepo.findByEmail(subject);
//                new IllegalArgumentException("User not found for email: " + subject);
//        return user.getUserId();
//    }
//}

    public UUID getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        // Check if token contains userId claim
        if (claims.get("userId") != null) {
            return UUID.fromString(claims.get("userId", String.class));
        }
        // Fallback to getting user by email
        String email = claims.getSubject();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email in token");
        }
        return user.getUserId();
    }

    public UUID getUserIdFromTokenOrEmail(String token, UserRepo userRepo) {
        String username = getUsernameFromToken(token);
        try {
            return UUID.fromString(username);
        } catch (IllegalArgumentException e) {
            User user = userRepo.findByEmail(username);
            if (user != null) {
                return user.getUserId();
            }
            throw new IllegalArgumentException("User not found for email: " + username);
        }
    }
}
