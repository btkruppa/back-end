package cooksys.service;

import org.springframework.stereotype.Service;

import cooksys.entity.Credentials;
import cooksys.entity.User;
import cooksys.repository.CredentialRepo;
import cooksys.repository.TagRepo;
import cooksys.repository.UserRepo;

@Service
public class ValidateService {

	private CredentialRepo credentialRepo;
	private UserRepo userRepo;
	private TagRepo tagRepo;

	public ValidateService(UserRepo userRepo, CredentialRepo credentialRepo, TagRepo tagRepo) {
		this.userRepo = userRepo;
		this.credentialRepo = credentialRepo;
		this.tagRepo = tagRepo;
	}

	public boolean doesUserExist(String username) {
		return (userRepo.findByUsernameAndActiveTrue(username) == null) ? false : true;
	}

	public boolean isUsernameValid(String username) {
		return (userRepo.findByUsernameIgnoringCase(username) == null) ? true : false;
	}

	public boolean doesTagExist(String label) {
		return (tagRepo.findByLabel(label) == null) ? false : true;
	}

	public User login(Credentials credentials) throws Exception {
		Credentials userCredentials = credentialRepo.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword());
		if (userCredentials == null) {
			throw new Exception("Invalid Credentials");
		}
		
		User user = userRepo.findByUsernameAndActiveTrue(userCredentials.getUsername());
		if (user != null) {
			return user;
		} else {
			throw new Exception("User does not exist");
		}
	}
}
