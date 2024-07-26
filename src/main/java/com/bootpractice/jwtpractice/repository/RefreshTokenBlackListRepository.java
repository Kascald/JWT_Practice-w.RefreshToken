package com.bootpractice.jwtpractice.repository;

import com.bootpractice.jwtpractice.entity.RefreshBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenBlackListRepository  extends JpaRepository<RefreshBlackList,Long> {
	@Transactional
	Boolean existsByRefreshToken(String refresh);

}
