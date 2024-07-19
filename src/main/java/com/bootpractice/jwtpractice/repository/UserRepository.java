package com.bootpractice.jwtpractice.repository;

import com.bootpractice.jwtpractice.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@EntityGraph(attributePaths = "roleList")
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	@Query("SELECT u from User u where LOWER(u.firstName) LIKE LOWER(:firstName) and LOWER(u.lastName) LIKE LOWER(:lastName)")
	Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

	Optional<User> findByEmail(String email);

	@Query("SELECT u from User u where u.legion LIKE (:legion)")
	Optional<List<User>> findByLegion(String legion);

	@Query("SELECT u from User u where u.gender LIKE (:gender)")
	Optional<List<User>> findByGender(String gender);
}
