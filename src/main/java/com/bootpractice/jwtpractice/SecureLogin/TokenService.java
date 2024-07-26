package com.bootpractice.jwtpractice.SecureLogin;

import com.bootpractice.jwtpractice.entity.RefreshBlackList;
import com.bootpractice.jwtpractice.entity.RefreshToken;
import com.bootpractice.jwtpractice.repository.RefreshTokenBlackListRepository;
import com.bootpractice.jwtpractice.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class TokenService {
	private final JWTTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

	public TokenService(JWTTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository, RefreshTokenBlackListRepository refreshTokenBlackListRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenBlackListRepository = refreshTokenBlackListRepository;
	}

	public Optional<RefreshToken> getRefreshToken(String username) {
		return refreshTokenRepository.findBySubject(username);
	}

	public Map<String ,String> refreshNewToken(String refresh) {

		String username = jwtTokenProvider.getUsernameFromToken(refresh);
		String email = jwtTokenProvider.getUserEmailFromToken(refresh);
		List<String> roleList = jwtTokenProvider.getRoleList(refresh);

		String newAccessToken = jwtTokenProvider.createAccessToken(username, roleList);
		String newRefreshToken = jwtTokenProvider.createRefreshToken(username, roleList);

		return Map.of("Access",newAccessToken , "Refresh",newRefreshToken);
	}

	public ResponseEntity<?> confirmExpired(String refresh) {
		if(refresh == null) {
			return new ResponseEntity<>("refresh token null", BAD_REQUEST);
		}
		try {
			jwtTokenProvider.isTokenExpiration(refresh);
		} catch (ExpiredJwtException e) {
			return new ResponseEntity<>("refresh token expired", BAD_REQUEST);
		}

		String category = jwtTokenProvider.tokenKindConfirm(refresh);

		if(!category.equals("Refresh")) {
			return new ResponseEntity<>("invalid refresh token", BAD_REQUEST);
		}

		if(!isExistsInDB(refresh)) {
			return new ResponseEntity<>("Invalid refresh token: not exists in DB", BAD_REQUEST);
		}

		return null;
	}

	public void newRefreshTokenSaveInCookie(HttpServletResponse response , String newRefreshToken) {
		jwtTokenProvider.saveRefreshTokenInCookie(response, cookieGenerator("Refresh-Token", newRefreshToken));
	}

	private Cookie cookieGenerator(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60 * 60 * 1000);
		cookie.setHttpOnly(true);

		return cookie;
	}
	public Boolean isExistsInDB(String refresh) {
		return refreshTokenRepository.existsByRefreshToken(refresh);
	}

	public void deleteRefreshToken(String refresh) {
		refreshTokenRepository.deleteByRefreshToken(refresh);
	}

	public void saveInBlackList(String refresh) {
		RefreshBlackList tokenBlacked = convertToRefreshBlack(refresh);
		refreshTokenBlackListRepository.save(tokenBlacked);
	}
	public void saveRefreshTokenInDB(String newRefresh) {
		if (jwtTokenProvider.tokenKindConfirm(newRefresh).equals("Refresh")) {
			refreshTokenRepository.save(convertToRefreshTokenEntity(newRefresh));
		}
	}

	private RefreshToken convertToRefreshTokenEntity(String refreshToken) {
		Claims refreshClaims = jwtTokenProvider.parsePayloadFromToken(refreshToken);
		RefreshToken tokenEntity = new RefreshToken();

		tokenEntity.setRefreshToken(refreshToken);
		tokenEntity.setSubject(refreshClaims.getSubject());
		tokenEntity.setExpiration(refreshClaims.getExpiration());

		return tokenEntity;
	}

	public RefreshBlackList convertToRefreshBlack(String refreshToken) {
		Claims refreshClaims = jwtTokenProvider.parsePayloadFromToken(refreshToken);
		RefreshBlackList blackToken = new RefreshBlackList();
		Date blackedDate = new Date();

		blackToken.setRefreshToken(refreshToken);
		blackToken.setUsername(refreshClaims.getSubject());
		blackToken.setBlackedDate(blackedDate);

		return blackToken;
	}

	public boolean isRefreshTokenValid(String refreshToken) {
		if (!jwtTokenProvider.validateToken(refreshToken)) {
			return false;
		}
		Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
		return foundRefreshToken.isPresent() && foundRefreshToken.get().getExpiration().after(new Date());
	}

	@Transactional
	public void moveExpiredTokenToBlackList() {
		Date now = new Date();
		List<RefreshToken> expiredTokens = refreshTokenRepository.findByExpirationBefore(now);

		for (RefreshToken token : expiredTokens) {
			RefreshBlackList blackList = new RefreshBlackList();

			blackList.setUsername(token.getSubject());
			blackList.setRefreshToken(token.getRefreshToken());
			blackList.setBlackedDate(now);

			refreshTokenBlackListRepository.save(blackList);
		}

		refreshTokenRepository.deleteByExpirationBefore(now);
	}


}
