package cooksys.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.Credential;
import cooksys.entity.Profile;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.projections.UserProjection;
import cooksys.repository.CredentialRepo;
import cooksys.repository.ProfileRepo;
import cooksys.repository.UserProjectionRepo;
import cooksys.repository.UserRepo;

@Service
public class UserService {

	private CredentialRepo credentialRepo;
	private UserRepo userRepo;
	private ProfileRepo profileRepo;
	private UserProjectionRepo userProjectionRepo;

	public UserService(UserRepo userRepo, CredentialRepo credentialRepo, ProfileRepo profileRepo,
			UserProjectionRepo userProjectionRepo) {
		this.userRepo = userRepo;
		this.credentialRepo = credentialRepo;
		this.profileRepo = profileRepo;
		this.userProjectionRepo = userProjectionRepo;
	}

	public User getByUsername(String username) {
		return userProjectionRepo.findByCredentialUsername(username);
	}

	public boolean doesUserExist(String username) {
		return (credentialRepo.findByUsernameIgnoringCase(username) == null) ? false : true;
	}

	public boolean doesProfileExist(String username) {
		return (profileRepo.findByEmailIgnoringCase(username) == null) ? false : true;
	}

	private boolean isCredentialValid(Credential credential) {
		return (credentialRepo.findByUsernameAndPassword(credential.getUsername(), credential.getPassword()) == null)
				? false : true;
	}

	@Transactional
	public void add(User user) {
		if (!doesUserExist(user.getCredential().getUsername())) {
			if (!doesProfileExist(user.getProfile().getEmail())) {
				// set the username in the profile as well as in the credentials
				// and neither can ever be updated. This is due to a circular
				// reference that was going on with users following each other
				user.getProfile().setUsername(user.getCredential().getUsername());
				credentialRepo.saveAndFlush(user.getCredential());
				profileRepo.saveAndFlush(user.getProfile());
				userRepo.saveAndFlush(user);
			}
		}
	}

	@Transactional
	public UserProjection update(User user) {
		if (isCredentialValid(user.getCredential())) {
			UserProjection oldUser = getByUsername(user.getCredential().getUsername());
			Profile oldUserProfile = oldUser.getProfile();
			Profile userProfile = user.getProfile();
			if (userProfile.getEmail() != null) {
				oldUserProfile.setEmail(userProfile.getEmail());
			}
			if (userProfile.getFirstName() != null) {
				oldUserProfile.setFirstName(userProfile.getFirstName());
			}
			if (userProfile.getLastName() != null) {
				oldUserProfile.setLastName(userProfile.getLastName());
			}
			if (userProfile.getPhoneNumber() != null) {
				oldUserProfile.setPhoneNumber(userProfile.getPhoneNumber());
			}
			profileRepo.save(oldUserProfile);
			return oldUser;
		}
		return null;
	}

	public List<Profile> get() {
		return profileRepo.findAll();
	}

	@Transactional
	public boolean follow(String username, Credential credential) {
		if (isCredentialValid(credential)) {
			User ourUser = userRepo.findByCredentialUsername(credential.getUsername());
			ourUser.getProfile().getFollowing().add(userRepo.findByCredentialUsername(username).getProfile());
			userRepo.save(ourUser);
			return true;
		} else {
			return false;
		}
	}

	public Set<Profile> getUserFollows(String username) {
		if (getByUsername(username) != null) {
			Set<Profile> followProjection = userRepo.findByCredentialUsername(username).getProfile().getFollowing();
			return followProjection;
		}
		return null;
	}

	public Set<Profile> getUserFollowers(String username) {
		if (getByUsername(username) != null) {
			return userRepo.findByCredentialUsername(username).getProfile().getFollowers();
		}
		return null;
	}

	public List<Tweet> getUserFeed(String username) {
		if (getByUsername(username) != null) {
			return userRepo.findByCredentialUsername(username).getProfile().getTweets();
		}
		return null;
	}
}
