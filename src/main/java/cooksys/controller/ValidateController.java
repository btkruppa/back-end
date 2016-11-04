package cooksys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.service.UserService;

@RestController
@RequestMapping("validate")
public class ValidateController {

	private UserService userService;

	public ValidateController(UserService service) {
		this.userService = service;
	}

	@GetMapping("username/exists/@{username}")
	public boolean doesUserExist(@PathVariable String username) {
		return userService.doesUserExist(username);
	}

	@GetMapping("username/available /@{username}")
	public boolean isUsernameValid(@PathVariable String username) {
		return !userService.doesUserExist(username);
	}
}
