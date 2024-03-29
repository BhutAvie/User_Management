package com.online.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.online.model.User;
import com.online.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = {"/", "/login"}, method=RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("user/login");
		return mav;
	}
	
	@RequestMapping(value = {"/signup"}, method=RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView mav = new ModelAndView();
		
		User user = new User();
		mav.addObject("user", user);
		mav.setViewName("user/signup");
		return mav;
	}
	
	@RequestMapping(value = {"/signup"}, method=RequestMethod.POST)
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		
		if(userExists != null) {
			bindingResult.rejectValue("email", "error.user", "This mail already exist");
		}
		if(bindingResult.hasErrors()) {
			mav.setViewName("user/signup");
		} else {
			 userService.saveUser(user);
			 mav.addObject("successMessage", "User has been registered successfully!");
			 mav.addObject("user", new User());
			 mav.setViewName("user/signup");
		}
		return mav;	
	}
	
	@RequestMapping(value = {"/home/home"}, method=RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		
		mav.addObject("userName", user.getFirstName() + " " + user.getLastName());
		mav.setViewName("home/home");
		return mav;
	}
	@RequestMapping(value = {"/access_denied"}, method=RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/access_denied");
		return mav;
	}
}