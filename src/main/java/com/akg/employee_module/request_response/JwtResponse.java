package com.akg.employee_module.request_response;

import com.akg.employee_module.model.Role;
import com.akg.employee_module.model.User;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwtToken;
	private User user;
	private Role role;

	public JwtResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	public JwtResponse(String jwtToken, User user,Role role) {
		this.jwtToken = jwtToken;
		this.user = user;
		this.role = role;
	}

	public String getToken() {
		return this.jwtToken;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}