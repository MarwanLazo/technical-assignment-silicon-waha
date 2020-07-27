package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AuthUser;

/**
 * 
 * @author Marwan
 *
 */
@Repository
public interface AuthUserRepository extends RootRepository<AuthUser, String> {

	AuthUser getUserByEmail(String email);

	@Query("select t from AuthUser t where t.username=:username")
	AuthUser getUserByUsername(@Param("username") String username);

}
