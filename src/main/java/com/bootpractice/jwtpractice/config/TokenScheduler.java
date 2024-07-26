package com.bootpractice.jwtpractice.config;


import com.bootpractice.jwtpractice.SecureLogin.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenScheduler {
	private final TokenService tokenService;

	public TokenScheduler(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Scheduled(cron = "0 0 * * * ?") //초  분  시  일  월  요일
//	@Scheduled(cron = "0 0/1 * 1/1 * ? *") //매 1분마다.
	public void scheduleCleaningRefreshToken() {
		tokenService.moveExpiredTokenToBlackList();
	}
}
