package com.example.demo.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "usertimesheet")
@Access(AccessType.FIELD)
public class UserTimesheet {

	@Id
//	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "id")
	private Integer id;

	@NotNull
	@OneToOne
	@JoinColumn(name = "email", referencedColumnName = "email")
	private AuthUser authUser;

	@NotNull
	@Column(name = "login_time")
	private Date logInTime;

	@NotNull
	@Column(name = "logout_time")
	private Date logOutTime;

//	@NotNull
//	@Column(name = "log_type")
//	@Enumerated(EnumType.STRING)
//	private LogType logType;

}
