package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MyConfig extends  WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService getUserDetailService()
	{
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());;
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());     ///////////////
		
		return daoAuthenticationProvider;
	}
	
	//configure method
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
				.authorizeHttpRequests()
				.antMatchers("/admin/**")
				.hasRole("ADMIN")
				.antMatchers("/user/**")
				.hasRole("USER")
				.antMatchers("/**")
				.permitAll()
				.and()
				.formLogin()                       //form based login system is used , which has feature of Logout
				.loginPage("/signin")            //our own login page designed
				.loginProcessingUrl("/dologin")            //On it the form need to be submitter, if success, it shows the user/index page , if fail the redirect to same page                                        //after this the default page of login will not come, instead our designed willcome .
				.defaultSuccessUrl("/user/index")
				//.failureUrl("/login-fail")         //can desing this page externally
				.and()
				.csrf().disable();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
