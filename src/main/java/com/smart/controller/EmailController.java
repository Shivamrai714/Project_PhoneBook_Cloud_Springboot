package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smart.entities.EmailRequest;
import com.smart.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@RequestMapping("/welcome")
	public String welcome()
	{
		return " Namaste ! this is my email.api ";
	}
	
	//  HAndler to send Email
	@RequestMapping(value="/sendemail" ,method=RequestMethod.POST)
	public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request)
	{
       		
		
		System.out.println(request);
	
		boolean results=this.emailService.sendEmail(request.getSubject(),request.getMessage(),request.getTo());
		
		if(results) {
		return ResponseEntity.ok("Email is sent successfully... :: ");
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email not sent");
		}
		
		
		//System.out.println(request);
		//return ResponseEntity.ok("dOne :: ");
		//rdgjwaxsbwejxsiz
		
	}
	
}
