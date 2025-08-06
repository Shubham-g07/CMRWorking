package com.SCM.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "Contact")
public class Contacts {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cId;
	
	private String cName;
	
	private String cNikName;
	
	private String cEmail;
	
	private String cNumber;
	
	private String cWork;
	
	private String cAddress;
	
	private String cImpDates;
	
	@Column(length = 5000)
	private String cDesc;
	
	@Transient
	private String cImgUrl;
	
	private String cImgName;
	
	
	private boolean cIsFav;
	
	@CreationTimestamp
	private LocalDateTime cAddedDate ;
	
	@UpdateTimestamp
	private LocalDateTime cUpdateedDate;
	
	@ManyToOne
	@JsonBackReference
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private User user;
	
	
	
	
	
	
}
