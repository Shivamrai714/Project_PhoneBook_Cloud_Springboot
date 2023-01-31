package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
 private UserRepository userRepository ;
	
	
	
	@RequestMapping("/")
	public String home(Model m)
	{
m.addAttribute("title","Home -Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m)
	{
m.addAttribute("title","About -Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m)
	{
m.addAttribute("title","Register Here -Smart Contact Manager");
m.addAttribute("user",new User());
		
return "signup";
	}
	
	//Handler for handle form / register user
	@PostMapping("/do_register")
	public String registerUser(@Valid   @ModelAttribute("user") User user, BindingResult result1   , @RequestParam(value="agreement", defaultValue = "false") Boolean agreement,Model model ,HttpSession session)
	{
		try {
			

			
			
			if(!agreement )
			{
				System.out.println("U have not agreed the terms and conditions");
			throw new Exception("U have not agreed the terms and conditions");
			}
			
			if(result1.hasErrors())
			{
				System.out.println("ERROR "+ result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
		    user.setPassword(passwordEncoder.encode(user.getPassword()));
						
			System.out.println("Agreement "+ agreement );
			System.out.println("USER "+ user);
			
			//save data to database
			
			User result = this.userRepository.save(user);
			model.addAttribute("user",new User());
			
			session.setAttribute("message", new Message("SuccessFully Registered !!" ,"alert-success"));

			return "signup";
			
			
		}catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("user",user);
		session.setAttribute("message", new Message("Something went woring !! "+ e.getMessage(), "alert-danger "));	
		return "signup";
		}

	}
	

	@GetMapping("/signin")
	public String customLogin(Model m)
	{
		m.addAttribute("title","The Login Page");
		return "login";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	//TO CHECK THE DATABASE CONNECTIVITY OVER HERE
	  @Autowired 
	  private UserRepository userRepository;
	  
	  @GetMapping("/test")
	  @ResponseBody 
	  public String test()
	  { 
		  User user= new User();
	 user.setName("Shivam"); 
	 user.setEmail("sjh14@gmail.com");
	 
	 userRepository.save(user);     //THIS WAS THE ERROR I WAS MISSING
	 
	 return "Working DAta may to added in database"; 
	 }
	 
	 */
	
	
	
	
	 
}
