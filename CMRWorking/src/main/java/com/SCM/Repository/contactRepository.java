package com.SCM.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SCM.Entities.Contacts;
import com.SCM.Entities.User;

@Repository	
public interface contactRepository extends JpaRepository<Contacts, Integer> {

	
	@Query("from Contacts as c where c.user.uId =:userId")
	public Page<Contacts> findContactsByUser(@Param("userId") int userId, Pageable pagable);
	
	
	public List<Contacts> findBycNameContainingAndUser(String cNameKeyword, User user);

}
