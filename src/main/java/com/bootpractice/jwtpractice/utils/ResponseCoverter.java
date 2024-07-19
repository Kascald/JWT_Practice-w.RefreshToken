package com.bootpractice.jwtpractice.utils;

import com.bootpractice.jwtpractice.dto.UserRes;
import com.bootpractice.jwtpractice.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseCoverter {
	UserRes userRes = null;
	public ResponseEntity<UserRes> zipSignUpResponse(User responsedUser, String responseMessage) throws Exception {
		try {
			userRes = ResponsePoolManager.borrowObject();

			userRes.setStatus("201");
			userRes.setMessage("account create success");
			userRes.setRequestURI("user/api/signup");
			userRes.setUsername(responsedUser.getUsername());
			userRes.setFirstname(responsedUser.getFirstName());
			userRes.setLastname(responsedUser.getLastName());

			return ResponseEntity.status(HttpStatus.CREATED).body(userRes);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (userRes != null) {
				ResponsePoolManager.returnObject(userRes);
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(userRes);
	}

	public ResponseEntity<UserRes> zipFindUserResponse(User foundUser, boolean isTrue) throws Exception {
		if (isTrue) {
			try {
				userRes = ResponsePoolManager.borrowObject();


				userRes.setStatus("200");
				userRes.setMessage("User found success");
				userRes.setRequestURI("user/api/find");
				userRes.setUsername(foundUser.getUsername());
				userRes.setFirstname(foundUser.getFirstName());
				userRes.setLastname(foundUser.getLastName());

				return ResponseEntity.status(HttpStatus.OK).body(userRes);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (userRes != null) {
					ResponsePoolManager.returnObject(userRes);
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(userRes);

		} else { // NotFound
			try {
				userRes = ResponsePoolManager.borrowObject();

				userRes.setStatus("404");
				userRes.setMessage("User Not Found");
				userRes.setRequestURI("user/api/find");
				userRes.setUsername(foundUser.getUsername());
				userRes.setFirstname(foundUser.getFirstName());
				userRes.setLastname(foundUser.getLastName());

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userRes);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (userRes != null) {
					ResponsePoolManager.returnObject(userRes);
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userRes);
		}

	}

}
