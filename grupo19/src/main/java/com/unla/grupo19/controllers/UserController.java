package com.unla.grupo19.controllers;

import java.util.Arrays;
import java.util.Optional;

import com.unla.grupo19.entities.UserRole;
import com.unla.grupo19.helpers.ViewHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.unla.grupo19.entities.User;
import com.unla.grupo19.services.implementation.UserRoleService;
import com.unla.grupo19.services.implementation.UserService;
import com.unla.grupo19.utils.Roles;


@Controller
@RequestMapping("/")
public class UserController {
	

	@Autowired
	private UserRoleService roleService;
	@Autowired
	private UserService userService;
	
	@GetMapping("/registro")
	public String registrar(Model model) {
		model.addAttribute("usuario", new User());  //model: interactua con la vista(html)registro.html
		return ViewHelper.REGISTER_PAGE;
	}
	
	@PostMapping("/save")
	public String guardar(@ModelAttribute("usuario")User user,Model model)  {
		System.out.println(user); 
		user.setRoles(Arrays.asList(roleService.traerPorNombre(Roles.ROLE_AUDITOR).get())); //se le setea el rol de audirot por defecto
		try {
			
		userService.saveOrUpdate(user);
		}catch (Exception e) {
			model.addAttribute("error",e.getMessage());
			return ViewHelper.REGISTER_PAGE;
		}
		return "redirect:/login";
	}
	
	
	@GetMapping("/login")
	public String login(Model model,@RequestParam(name="error",required=false)String error,
	@RequestParam(name="logout", required=false)String logout) {
		model.addAttribute("error",error);
		model.addAttribute("logout",logout);
		return ViewHelper.LOGIN_PAGE;
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		return "redirect:/"+ViewHelper.LOGIN_PAGE;
	}

	@GetMapping("/loginsuccess")
	public ModelAndView loginCheck() {
		ModelAndView mav = new ModelAndView(ViewHelper.HOME_PAGE);
		User user = userService.findByUsernameQuery(SecurityContextHolder.getContext().getAuthentication().getName());
		mav.addObject("isAdmin", userService.isAdmin(user));
		return mav;
	}
	
	@GetMapping("/cancel")
	public String volver() {
		return "redirect:/login";
		
	}

}