package com.SCM.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SCM.Entities.User;

@Repository
public interface userRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.uEmail =:email")
	public User getUserByUserName(@Param("email") String email);
	
	public boolean existsByuEmail(String email);
}
