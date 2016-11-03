package cooksys.service;

import org.springframework.stereotype.Service;

import cooksys.repository.CredentialRepo;
import cooksys.repository.UserRepo;

@Service
public class UserService {

	private CredentialRepo credentialRepo;
	private UserRepo userRepo;

	public UserService(UserRepo userRepo, CredentialRepo credentialRepo) {
		this.userRepo = userRepo;
		this.credentialRepo = credentialRepo;
	}
}
