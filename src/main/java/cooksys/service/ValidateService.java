package cooksys.service;

import org.springframework.stereotype.Service;

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
		return (userRepo.findByUsernameAndActiveTrue(username) == null) ? false : true;
	}
}
