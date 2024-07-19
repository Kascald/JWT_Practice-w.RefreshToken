package com.bootpractice.jwtpractice.repository;

import com.bootpractice.jwtpractice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findBySubject(String subject);
	void deleteByRefreshToken(String refreshToken);
}
