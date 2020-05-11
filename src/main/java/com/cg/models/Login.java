package com.cg.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity(name = "logintable")
public class Login {

	@Id
	private String adminId;
	@Column(unique = true)
	private String adminPass;

	@Column
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Login() {

	}

	public Login(String adminId, String adminPass, String role) {

		this.adminId = adminId;
		this.adminPass = BCrypt.hashpw(adminPass, BCrypt.gensalt());
		this.role = role;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = BCrypt.hashpw(adminPass, BCrypt.gensalt());
	}
	
//	{
//		 "empId": 100005,
//       "fname": "Iowa",
//       "lname": "Lewis",
//       "doj": "21-30-2018",
//       "dob": "01-10-1998",
//       "department": "Executive",
//       "grade": "M3",
//       "designation": "CEO",
//       "gender": "male",
//       "salary": 490000.0,
//       "maritalStatus": "Married",
//       "address": "Sangola",
//       "managerId": 100001,
//       "password": "1012"
//}

}
