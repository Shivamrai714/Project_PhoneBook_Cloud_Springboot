package com.smart.controller;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
/*  -------------------------------------------------  */	
	
// adding the common hander to assign the logged in : user to all the handler to use it;
	                                       //STEPP : 20 
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
     {
		 String username = principal.getName();
	     System.out.println("USER_NAME"+username);
	     
	     User user= userRepository.getUserByUserName(username);
	     model.addAttribute("user",user);
	     System.out.println(user);
	
     }
	
/*  -------------------------------------------------  */	
	//dashboard home
	
	@RequestMapping("/index")
	public String dashboard(Model model , Principal principal)
	{
		model.addAttribute("title","User DashBoard");
		
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
       model.addAttribute("person",user.getName());
       model.addAttribute("date", new Date());
 return "normal/user_dashboard";
	}
	
/*  -------------------------------------------------  */	
	
	// open the add form handler   //STEEP 21 : taking common user for all handlers and desing the form .
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");	
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	
	
/*  -------------------------------------------------  */	
	
	//STEP 23 :  Submit Contact Form
	//handle the submission of Contact Form of action   : /user/process-contact : user already included, so handler the process-contact only
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact ,
			Principal principal,
			@RequestParam("profileImage") MultipartFile file,
			HttpSession session
			)
	{
		System.out.println(file.getOriginalFilename());
		
		try {
			String name=principal.getName();
			User user = this.userRepository.getUserByUserName(name);
	
			 //to test the error cond we can throw error ef if(3>2) throw new Exception(); 
			
			//STEPP 24 : ADDING CONTACT IMAGE
			
			// profile image processing and uploading
			// The file will be addded to target folder / its folder after the project built
			if(file.isEmpty())
			{
				System.out.println("File is empty");
			  //set default photo
				contact.setImage("contact.png");
			}
			else{
				//set the file to folder ,and put in contact field of image.
				
				contact.setImage(file.getOriginalFilename());
			    
				
				File saveFile = new ClassPathResource("static/img").getFile();	
			    Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			    
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("File uploaded successfully");
			}
			
			
			
			contact.setUser(user); // saved as bi directional mapping exists

			user.getContacts().add(contact); // contact added to user
			
			this.userRepository.save(user);
			
			
			System.out.println("DATA"+ contact);
			
			System.out.println("Contact added to database");		
		//message success added successfully
			session.setAttribute("message", new Message("Your Contact is Added !! Add More","success"));          //use the helper class to send the content and  type with msg
			
		} catch (Exception e) {
			System.out.println("ERROR : "+ e.getMessage());
			e.printStackTrace();

			//message error : user not added 
			session.setAttribute("message", new Message("Some Thing went worong !!Try Again","danger"));          //use the helper class to send the content and  type with msg
			
		}
		
		return "normal/add_contact_form";
		
	}


/*  ----------------------------- -------------------  */	
	
	//STEP 24

	//Show contacts Handler

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal)
	{
		m.addAttribute("title","Show User Contacts");
		
		//STEP 26
		//send the list of contact from database 
		//make Seperate Contact Repository  -> helpful in creating the pagenateion Concepts
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page, 5);                   //Pageable is parent interface 
	Page<Contact> contacts=	this.contactRepository.findContactByUser(user.getId(),pageable);
		
	m.addAttribute("contacts",contacts);
	m.addAttribute("currentPage",page);	
	m.addAttribute("totalPages",contacts.getTotalPages());
	//STEP 27   PAGE-NATION
	//per page-5 contacts  (n)
	//current page- 0       (page)
		
	//Use path varaible to get the current page. ALso return the page from the ContactRepository.
	//Change return type to page type and addAttibutes of current page etc

		return "normal/show_contacts";
	}
	
	
	
/*----------------------------- -------------------  */	
	// STEPP 30 : Handler for the email click to show user profile
	
	@GetMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid , Model model,Principal principal)
	{
		//System.out.println("CID "+cid);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		
		
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		//STEPP 31: clearing bug of random contact-access
		
		if(user.getId() == contact.getUser().getId())    //send the valid contactss to users onlu
		{  
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getName());
		}
		
		
		
		
		
		
		return "normal/contact_detail";
		
		
		
		
		
	}
	
	
	
	
	
	
/*----------------------------- -------------------  */	
	//STEPP : 32
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid ,Model m , HttpSession session ,Principal principal)
	{
		System.out.println("CID : "+ cid);
		Contact contact = this.contactRepository.findById(cid).get();
		
		//apply check that , person deleteing the contact should be actual logged in user.

		
		//unlink the user , problem due to cascade can be resolved
		
		
		
		//STEP :35  REMOVING THE CONTACT FROM DATA BASE USE the 
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		
		user.getContacts().remove(contact);    //TO remove the current contact of Delete button , JVM interanlly uses the equals method of COntact class , so override it.
		this.userRepository.save(user);
		
		
		session.setAttribute("message", new Message("Contact deleted Successfully" , "success"));
		return "redirect:/user/show-contacts/0";
	}
	
	
	
/*----------------------------- -------------------  */	
	//STEPP 33
	//open update form folder
	
	//post is secure mapping , so safety is ensured.
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m)
	{
		m.addAttribute("title","Update Contact");
		Contact contact = this.contactRepository.findById(cid).get();
		
		m.addAttribute("contact",contact);
		return "/normal/update_form";           //Will Show the data on new form for the update opeitonss
	}
	
	
	
/*----------------------------- -------------------  */	
	//STEPP :33 
	
	// the update form submission
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Model m , HttpSession session,Principal principal)
	{
	
		try {
			
			//fetch old contact details
			
			Contact oldContactDetail = this.contactRepository.findById(contact.getCid()).get();
			
			
			//if new image chosen
			if(!file.isEmpty())
			{
				//process file - rewrite 
				
				//1 detlete the old photo from folder : get the path
				
				File deletefile = new ClassPathResource("static/img").getFile();
				File file1= new File(deletefile,oldContactDetail.getImage());
				file1.delete();
				 
				 
				//2.update the new Photo in file 
				
				File savefile = new ClassPathResource("static/img").getFile();
				 Path path = Paths.get(savefile.getAbsolutePath()+File.separator+ file.getOriginalFilename());
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());  //saving the name in database;
				
			}  //file is empty only : same as old
			else {
				contact.setImage(oldContactDetail.getImage());
			}
			
			//saving the current user, to reflect bacj the changes of updated contact.
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
		
			
			session.setAttribute("message",new Message("Your contact updated ","success") );
			
			
		} catch (Exception e) {
			
			
		}
		

		System.out.println("CONTACT"+contact.getName());
		
		return "redirect:/user/"+contact.getCid() + "/contact";
	}

/*----------------------------- -------------------  */	
//STEP 37 :
	
	// your profile handler
	@GetMapping("/profile")           // /user/profile  : it will be actually like this
	public String yourProfile(Model m)
	{
m.addAttribute("title","Profile Page");

return  "normal/profile";
	}
	
	

	
/*----------------------------- -------------------  */	
	
}
