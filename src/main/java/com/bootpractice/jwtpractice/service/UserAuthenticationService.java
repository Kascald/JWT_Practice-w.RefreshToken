package com.bootpractice.jwtpractice.service;

import com.bootpractice.jwtpractice.dto.CustomUserDetails;
import com.bootpractice.jwtpractice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthenticationService implements UserDetailsService {

	private final UserFindService userFindService;
	private final Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

	public UserAuthenticationService(UserFindService userFindService) {
		this.userFindService = userFindService;
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//		UserDTO user = userFindService.findUserDTOByUsername(username);
		User user = userFindService.findByUsername(username);
		logger.info("user = {}",user.getUsername());

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		return new CustomUserDetails(user);
	}
}
