package com.bootpractice.jwtpractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class RefreshToken {
	@Id
	private String subject;
	private Date expiration;
	private String refreshToken;
}
