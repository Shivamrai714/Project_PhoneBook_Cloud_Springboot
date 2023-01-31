package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;
//STEP 26
public interface ContactRepository extends JpaRepository<Contact, Integer> 
{
	//Helpful in pagenation Concepts
	
	//writing manual query to fetch the contacts of LOgged in User
	@Query("from Contact as c where c.user.id=:userId")
	public  Page<Contact> findContactByUser(@Param("userId") int userId , Pageable pageable);
	
	
	//STEP 28 : 
	//changing the return type of Func to Page<Contact>  from list type. ans also addding parameter of Pageble 
	//Pageble has (current page, contact per page) 
	
	
	//STEP 35 
	//SEARCH BAR 
	//mathc the Contact , which matches with current User
	
	public List<Contact> findByNameContainingAndUser(String name,User user);    //springboot itslef gives the defination of this funtion.
	
	
	
	

}







/*
 * 
 //writing manual query to fetch the contacts of LOgged in User
	@Query("from Contact as c where c.user.id=:userId")
	public  List<Contact> findContactByUser(@Param("userId") int userId);
	
 * 
 * 
 */
