package com.SCM.Entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "USER")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int uId;
	
	
	@NotBlank ( message="Name cannot be empty !!")
	@Size ( min = 2,max = 20, message="Enter name in between 2-20 characters")
	@Pattern ( regexp = "^(?=.*[a-zA-Z])[a-zA-Z ]+$", message = "Name must be alphabetic")
	private String uName;
	
	
	
	@Column ( unique = true)
	@NotBlank ( message="Email is required !!")
	@Email ( regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message="Enter valid email")
	private String uEmail;
	
	
	
	@NotBlank ( message = "Password is required !!")
	@Pattern(
		    regexp = ".*[A-Z].*",
		    message = "Password must contain at least one uppercase letter"
		)
	@Pattern(
		    regexp = ".*[a-z].*",
		    message = "Password must contain at least one lowercase letter"
		)
	@Pattern(
		    regexp = ".*\\d.*",
		    message = "Password must contain at least one digit"
		)
	@Pattern(
		    regexp = ".*[@#$%^&+=!].*",
		    message = "Password must contain at least one special character"
		)
	private String uPassword;
	
	private String uAddress;
	
	@Transient
	private MultipartFile uImgFile;
	
	private String uImgName;
	
	private LocalDate uDOB;
	
	
	@Column(length = 500)
	private String uAbout;
	
	private String uRole;
	
	private boolean uEnable;

	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
	@JsonManagedReference
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Contacts> contact = new HashSet<>();
	
	
}
