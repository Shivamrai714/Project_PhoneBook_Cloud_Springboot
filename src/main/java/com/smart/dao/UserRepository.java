package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;

//No need to use @service , as its inside out main smart package.
public interface UserRepository  extends JpaRepository<User,Integer>{

	//write jpa query
	@Query("select u from User u where u.email=:email")
	public User getUserByUserName(@Param("email") String email);

	
}
