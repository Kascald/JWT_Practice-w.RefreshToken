package com.bootpractice.jwtpractice.config;


import com.bootpractice.jwtpractice.SecureLogin.CustomLoginFilter;
import com.bootpractice.jwtpractice.SecureLogin.JWTFilter;
import com.bootpractice.jwtpractice.SecureLogin.JWTTokenProvider;
import com.bootpractice.jwtpractice.repository.RefreshTokenRepository;
import com.bootpractice.jwtpractice.utils.PasswordHasher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	@Bean
	public PasswordHasher passwordHasher() {
		return new PasswordHasher(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//For jwt cors config
		http.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration configuration = new CorsConfiguration();

				configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
				configuration.setAllowedMethods(Collections.singletonList("*"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Collections.singletonList("*"));
				configuration.setMaxAge(3600L);

				return configuration;
			}
		})));

		http.csrf((auth)-> auth.disable());
		http.formLogin((auth)-> auth.disable());
		http.httpBasic((auth)-> auth.disable());

		/* Lambda Expression
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable); */

		http.authorizeHttpRequests((auth)-> auth
				.requestMatchers("/user/api/login", "/", "/user/api/signup","/user/**","login").permitAll()
//				.requestMatchers("/roleTest/**").hasAuthority("ADMIN")
				.requestMatchers("/roleTest/**").hasRole("ADMIN")
				.anyRequest().authenticated());

		http.addFilterBefore(new JWTFilter(jwtTokenProvider), CustomLoginFilter.class);

		http.addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration),jwtTokenProvider,refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}
}
