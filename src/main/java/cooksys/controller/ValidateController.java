package cooksys.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.service.UserService;
import cooksys.service.ValidateService;

@RestController
@CrossOrigin
@RequestMapping("validate")
public class ValidateController {

	private UserService userService;
	private ValidateService validateService;

	public ValidateController(UserService service, ValidateService validateService) {
		this.userService = service;
		this.validateService = validateService;
	}

	@GetMapping("username/exists/@{username}")
	public boolean doesUserExist(@PathVariable String username) {
		return validateService.doesUserExist(username);
	}

	@GetMapping("username/available/@{username}")
	public boolean isUsernameValid(@PathVariable String username) {
		return validateService.isUsernameValid(username);
	}

	@GetMapping("tag/exists/{label}")
	public boolean doesTagExist(@PathVariable String label) {
		return validateService.doesTagExist("#" + label);
	}
}
