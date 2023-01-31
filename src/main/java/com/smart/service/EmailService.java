package com.smart.service;





import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service                 //or use it @Component
public class EmailService {

	public boolean sendEmail(String subject,String message,String to)
	{
	//copy paste the code from sending mail in java video 
		boolean flag=false;
		
		String from="shivamraiias@gmail.com";
		
		
		//variable for gmail.
		
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties=System.getProperties();
		System.out.println("PROPERTIES"+properties);
		
		//setting imp info to properties object.
		
		//host set
		
		properties.put("mail.smtp.host",host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//STEP 1 : to gt the sesssion object:
		Session session=Session.getInstance(properties,new Authenticator() 
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication("shivamraiias@gmail.com", "rdgjwaxsbwejxsiz");
			}
		
		});
		
		session.setDebug(true);
		
		//Step 2 : compare the message [text , multi media]

		MimeMessage m = new MimeMessage(session);
		
		try {
			//from email
			m.setFrom(from);
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			//adding subject to message
			m.setSubject(subject);
			//adding text to message
			m.setText(message);
		
     //STEP 3 : send the message using Transport class
			Transport.send(m);
			System.out.println("Sent success................");
			flag=true;
			
			
		}catch (Exception e) 
		{
		e.printStackTrace();	
		}
		return flag;
	}
	
	
}
