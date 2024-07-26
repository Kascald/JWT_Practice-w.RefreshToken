package com.bootpractice.jwtpractice.SecureLogin;


import com.bootpractice.jwtpractice.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTTokenProvider {

	private final SecretKey mySecretKey;
	private final RefreshTokenRepository refreshTokenRepository;

	//	@Value("${accessTokenExpiration}")
	private final long ACCESS_EXPIRATION_PERIOD;   //10 min => milliseconds
	//	@Value("${refreshTokenExpiration}")
	private final long REFRESH_EXPIRATION_PERIOD;   //1 hour => milliseconds


	public JWTTokenProvider(RefreshTokenRepository refreshTokenRepository, @Value("${myJwtRandomKeyHashed}")String jwtSecretKey,
	                        @Value("${accessTokenExpiration}")long accessExpiration, @Value("${refreshTokenExpiration}")long refreshExpiration) {
		this.refreshTokenRepository = refreshTokenRepository;
		mySecretKey = new SecretKeySpec(jwtSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
		ACCESS_EXPIRATION_PERIOD = accessExpiration;
		REFRESH_EXPIRATION_PERIOD = refreshExpiration;
	}

	public String generateToken(String username
			, List<String> userRole, Long tokenExpiration, String tokenKind) {
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		Instant now = ZonedDateTime.now(zoneId).toInstant();
		Date issuedAt = Date.from(now);
		Date expiration = Date.from(now.plusMillis(tokenExpiration));
		Map<String, Object> claims = new HashMap<>();
		claims.put("userRole", userRole);

		return Jwts.builder()
				.subject(username)
				.claim("token_kind",tokenKind)
				.claims(claims)
				.signWith(mySecretKey)
				.issuedAt(issuedAt)
				.expiration(expiration)
				.encodePayload(true)
				.compact();
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parser().verifyWith(mySecretKey).build().parseSignedClaims(token).getPayload().get("sub", String.class);
	}

	public List<String> getRoleList(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(mySecretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		List<?> rawList = claims.get("userRole", List.class);
		List<String> roleList = null;

		if (rawList != null) {
			roleList = rawList.stream()
					.filter(item -> item instanceof String)
					.map(item -> (String) item)
					.collect(Collectors.toList());
		}

		return roleList;
	}

	public String createAccessToken(String username,  List<String> userRole) {
		return generateToken(username, userRole, ACCESS_EXPIRATION_PERIOD, "Access");
	}

	public String createRefreshToken(String username, List<String> userRole) {
		return generateToken(username,  userRole, REFRESH_EXPIRATION_PERIOD, "Refresh");
	}



	public String tokenKindConfirm(String refreshToken) {
		return Jwts.parser().verifyWith(mySecretKey).build().parseSignedClaims(refreshToken).getPayload().get("token_kind", String.class);
	}



	public Claims parsePayloadFromToken(String token) throws JwtException {
		return Jwts.parser()
				.verifyWith(mySecretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	public String getUserEmailFromToken(String token) throws JwtException {
		return parsePayloadFromToken(token).getSubject();
	}

	public boolean validateToken(String token) {
		try {
			parsePayloadFromToken(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public boolean isTokenExpiration(String token) {
		return Jwts.parser().verifyWith(mySecretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	}

	public void setAuthorizationHeaderForAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("Authorization", "Bearer "+ accessToken);
	}

	public void setAuthorizationHeaderForRefreshToken(HttpServletResponse response, String refreshToken) {
		response.setHeader("Refresh-Token", "Bearer "+ refreshToken);
	}

	public void saveRefreshTokenInCookie(HttpServletResponse response , Cookie cookie) {
		response.addCookie(cookie);
	}

}
