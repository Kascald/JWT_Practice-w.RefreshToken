package com.bootpractice.jwtpractice.entity;


import com.bootpractice.jwtpractice.dto.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class User{

	@Id @Column(unique = true, nullable = false)
	private UUID id;

	private String username;
	private String password;

	private String firstName;
	private String lastName;

	private String email;
	private String gender;
	private String birthDay;

	private String phone;
	private String legion;

	@ElementCollection
	private List<String> roleList;

	// 생성자 또는 @PrePersist 메소드에서 id를 초기화
	@PrePersist
	public void initializeUUID() {
		if (id == null) {
			id = UUID.randomUUID();
		}
	}

	public User extractionFromSignUpRequest(SignUpRequest signUpRequest) {
		return User.builder()
				//.id() Auto Creation
				.username(signUpRequest.getEmail())
				//password() Setter로 추가할 것
				.firstName(signUpRequest.getFirstName())
				.lastName(signUpRequest.getLastName())
				.email(signUpRequest.getEmail())
				.gender(signUpRequest.getGender())
				.phone(signUpRequest.getPhone())
				.legion(signUpRequest.getLegion())
				.build();
	}
}
