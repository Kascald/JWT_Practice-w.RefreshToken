package com.bootpractice.jwtpractice.exception;


import lombok.*;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class DefaultErrorResponse {
	private int status;
	private String code;
	private String description;

	public static ResponseEntity<DefaultErrorResponse> toDefaultErrorResponse(
			UserServiceErrorCode e) {
		return ResponseEntity.status(e.getHttpStatus())
				.body(DefaultErrorResponse.builder()
						      .status(e.getHttpStatus().value())
						      .code(e.name())
						      .description(e.getMessage())
						      .build()
				     );
	}
}
