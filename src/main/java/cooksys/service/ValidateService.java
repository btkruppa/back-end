package cooksys.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.User;
import cooksys.repository.CredentialRepo;
import cooksys.repository.ProfileRepo;
import cooksys.repository.UserRepo;

@Service
public class ValidateService {

	private CredentialRepo credentialRepo;
	private UserRepo userRepo;
	private ProfileRepo profileRepo;

	public ValidateService(UserRepo userRepo, CredentialRepo credentialRepo, ProfileRepo profileRepo) {
		this.userRepo = userRepo;
		this.credentialRepo = credentialRepo;
		this.profileRepo = profileRepo;
	}

	public boolean doesUserExist(String username) {
		return (userRepo.findByCredentialUsername(username) == null) ? false : true;
	}

	@Transactional
	public void add(User user) {
		credentialRepo.saveAndFlush(user.getCredential());
		profileRepo.saveAndFlush(user.getProfile());
		userRepo.saveAndFlush(user);
	}
}
