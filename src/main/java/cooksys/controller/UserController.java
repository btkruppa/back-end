package cooksys.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.entity.Credentials;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.request_models.CreateProfileRequestModel;
import cooksys.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("users")
public class UserController {

	private UserService userService;

	public UserController(UserService service) {
		this.userService = service;
	}

	@GetMapping
	public List<User> get() {
		return userService.get();
	}

	@GetMapping("/@{username}")
	public User getUserByName(@PathVariable String username) throws Exception {
		return userService.getByUsername(username);
	}

	@GetMapping("/@{username}/following")
	public Set<User> getUserFollows(@PathVariable String username) throws Exception {
		return userService.getUserFollows(username);
	}

	@GetMapping("/@{username}/followers")
	public Set<User> getUserFollowers(@PathVariable String username) throws Exception {
		return userService.getUserFollowers(username);
	}

	@GetMapping("/@{username}/tweets")
	public List<Tweet> getUserTweets(@PathVariable String username) throws Exception {
		return userService.getUserTweets(username);
	}

	@GetMapping("/@{username}/mentions")
	public List<Tweet> getUserMentions(@PathVariable String username) throws Exception {
		return userService.getUserMentions(username);
	}

	@GetMapping("/@{username}/feed")
	public List<Tweet> getUserFeed(@PathVariable String username) throws Exception {
		return userService.getUserFeed(username);
	}

	@PostMapping
	public User add(@RequestBody CreateProfileRequestModel createProfileRequestModel) throws Exception {
		return userService.add(createProfileRequestModel);
	}

	@PatchMapping("/@{username}")
	public User patch(@PathVariable String username, @RequestBody CreateProfileRequestModel createProfileRequestModel)
			throws Exception {
		if (createProfileRequestModel.getCredentials().getUsername().equals(username)) {
			return userService.update(createProfileRequestModel);
		} else {
			throw new Error("Username does not match credentials");
		}
	}

	@PostMapping("/@{username}/follow")
	public void follow(@PathVariable String username, @RequestBody Credentials credentials) throws Exception {
		userService.follow(username, credentials);
	}

	@PostMapping("/@{username}/unfollow")
	public void unFollow(@PathVariable String username, @RequestBody Credentials credentials) throws Exception {
		userService.unFollow(username, credentials);
	}

	@DeleteMapping("/@{username}")
	public User delete(@PathVariable String username, @RequestBody Credentials credentials) throws Exception {
		if (username.equals(credentials.getUsername())) {
			return userService.delete(username, credentials);
		}
		throw new Exception("Username provided does not match the credentials");
	}
}
