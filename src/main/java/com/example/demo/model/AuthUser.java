package com.example.demo.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@ToString

@Entity
@Table(name = "loguser")
@Access(AccessType.FIELD)
public class AuthUser {

	@Id
	@Email
	@NotNull
	@Column(name = "email")
	private String email;

	@NotNull
	private String username;

	@Size(min = 3, max = 8)
	@NotNull
	@Column(name = "password")
	private String password;

	
	@Column(name = "mobile")
	private String mobile;

}
