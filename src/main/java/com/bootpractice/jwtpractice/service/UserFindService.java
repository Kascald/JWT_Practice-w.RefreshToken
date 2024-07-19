package com.bootpractice.jwtpractice.service;

import com.bootpractice.jwtpractice.entity.User;
import com.bootpractice.jwtpractice.exception.UserServiceExceptions;
import com.bootpractice.jwtpractice.dto.UserDTO;
import com.bootpractice.jwtpractice.repository.UserRepository;
import com.bootpractice.jwtpractice.utils.PasswordHasher;
import com.bootpractice.jwtpractice.utils.ResponseCoverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bootpractice.jwtpractice.exception.UserServiceErrorCode.USER_NOT_FOUND;

@Service
public class UserFindService implements UserServiceGeneral{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserRepository userRepository;
	private final ResponseCoverter responseCoverter;
	private final PasswordHasher passwordHasher;

	public UserFindService(UserRepository userRepository, ResponseCoverter responseCoverter, PasswordHasher passwordHasher) {
		this.userRepository = userRepository;
		this.responseCoverter = responseCoverter;
		this.passwordHasher = passwordHasher;
	}

	//유저 회원가입 아이디 기반 조회
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public UserDTO findUserDTOByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		return new UserDTO(user);
	}

	//유저 실명 기반 조회
	public User findByUserRealName(String firstName, String LastName) {
		return userRepository.findByFirstNameAndLastName(firstName, LastName).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND));
	}
	//유저 정밀 조회 - 추후 구현

	//유저 이메일 기반 조회
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND));
	}
	//유저 중복조회
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	//유저 상태 조회
	//유저 역할 기반 조회

	//유저정보 기반 필터링 정보제공 - legion
	public List<User> findByLegion(String legion) {
		return userRepository.findByLegion(legion).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND));
	}

	//유저정보 기반 필터링 정보 제공 - gender
	public List<User> findByGender(String gender) {
		return userRepository.findByGender(gender).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND));
	}

	//유저 정보 2개 비교
	public boolean compareUserInfo(User inputUserInfo, User foundeUserInfo) {
		loggingObject(inputUserInfo,logger);
		loggingObject(foundeUserInfo,logger);

		String foundName = foundeUserInfo.getFirstName() + foundeUserInfo.getLastName();
		String inputName = inputUserInfo.getFirstName() + inputUserInfo.getLastName();
		int compareScore = 0;

		if (inputUserInfo.getId() != null && inputUserInfo.getId().equals(foundeUserInfo.getId()))
			compareScore++;

		if (inputName.equals(foundName) )
			compareScore++;

		if (inputUserInfo.getPhone() != null && inputUserInfo.getPhone().equals(foundeUserInfo.getPhone()))
			compareScore++;

		if (inputUserInfo.getEmail() != null && inputUserInfo.getEmail().equals(foundeUserInfo.getEmail()))
			compareScore++;

		return compareScore > 0;
	}



}
