package com.akg.employee_module.controller.rest;

import com.akg.employee_module.config.JwtTokenUtil;
import com.akg.employee_module.model.Role;
import com.akg.employee_module.model.User;
import com.akg.employee_module.service.CommonMethodService;
import com.akg.employee_module.service.JwtUserDetailsService;
import com.akg.employee_module.API;
import com.akg.employee_module.request_response.JwtRequest;
import com.akg.employee_module.request_response.JwtResponse;
import com.akg.employee_module.request_response.Response;
import com.akg.employee_module.service.SeEventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private CommonMethodService commonMethodService;

	@Autowired
	private SeEventLog seEventLog;

	@RequestMapping(value = API.check)
	public ResponseEntity<Object> checkServer(){
		return ResponseEntity.ok(new Response(true,"Server Running"));
	}

	@RequestMapping(value = API.authenticate, method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
			HttpServletRequest request, @RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		System.out.println(userDetails);
		final String token = jwtTokenUtil.generateToken(userDetails);

		// Modified by Programmer
		User user = userDetailsService.getUserFromDB(authenticationRequest.getUsername());
		Role role = commonMethodService.getUserRole(user.getId().intValue());

		//Logging Login Event
		seEventLog.createEventLog(user,request,"User Login",null,null,null);
		return ResponseEntity.ok(new JwtResponse(token,user,role));
		// Modified by Programmer
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
