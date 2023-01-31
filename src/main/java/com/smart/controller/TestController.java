package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Controller
public class TestController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/test")
	@ResponseBody
	public String test()
	{
		
		
		User user= new User();
		user.setEmail("Shivam@Gami.com");
		user.setId(1);
		user.setName("Shivam");
		user.setPassword("Shivam");
		user.setRole("ROLE_NORMAL");
		userRepository.save(user);
		
		return "This is just for testing";
	}
	
}
