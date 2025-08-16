package com.SCM.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.SCM.Entities.Contacts;
import com.SCM.Entities.User;
import com.SCM.Repository.contactRepository;
import com.SCM.Repository.userRepository;

@RestController
public class searchController {
	
	@Autowired
	public userRepository userrepo;
	
	@Autowired
	public contactRepository contactrepo;
	
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
		
		String name = principal.getName();
		User user = userrepo.getUserByUserName(name);
		
		System.out.println(query);
		List<Contacts> searchResult = contactrepo.findBycNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(searchResult);
		
	}
	
	
}
