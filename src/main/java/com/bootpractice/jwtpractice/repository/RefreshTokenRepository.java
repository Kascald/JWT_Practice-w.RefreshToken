package com.bootpractice.jwtpractice.repository;

import com.bootpractice.jwtpractice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findBySubject(String subject);
	boolean existsByRefreshToken(String refreshToken);
	@Transactional
	void deleteByRefreshToken(String refreshToken);

	List<RefreshToken> findByExpirationBefore(Date now);
	void deleteByExpirationBefore(Date now);
}
