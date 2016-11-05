package cooksys.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.Credential;
import cooksys.entity.Profile;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.repository.CredentialRepo;
import cooksys.repository.ProfileRepo;
import cooksys.repository.UserRepo;
import cooksys.request_models.CreateProfileRequestModel;

@Service
public class UserService {

	private CredentialRepo credentialRepo;
	private UserRepo userRepo;
	private ProfileRepo profileRepo;

	public UserService(UserRepo userRepo, CredentialRepo credentialRepo, ProfileRepo profileRepo) {
		this.userRepo = userRepo;
		this.credentialRepo = credentialRepo;
		this.profileRepo = profileRepo;
	}

	public User getByUsername(String username) {
		return userRepo.findByUsername(username);
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
	public User add(CreateProfileRequestModel createProfileRequestModel) throws Exception {
		if (!doesUserExist(createProfileRequestModel.getCredential().getUsername())) {
			if (!doesProfileExist(createProfileRequestModel.getProfile().getEmail())) {
				User user = new User();
				user.setUsername(createProfileRequestModel.getCredential().getUsername());
				user.setProfile(createProfileRequestModel.getProfile());
				credentialRepo.saveAndFlush(createProfileRequestModel.getCredential());
				profileRepo.saveAndFlush(createProfileRequestModel.getProfile());
				return userRepo.saveAndFlush(user);
			}
		}
		throw new Exception("User already exists");
	}

	@Transactional
	public User update(CreateProfileRequestModel createProfileRequestModel) {
		if (isCredentialValid(createProfileRequestModel.getCredential())) {
			User oldUser = getByUsername(createProfileRequestModel.getCredential().getUsername());
			Profile oldUserProfile = oldUser.getProfile();
			Profile userProfile = createProfileRequestModel.getProfile();
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

	public List<User> get() {
		return userRepo.findAll();
	}

	@Transactional
	public boolean follow(String username, Credential credential) {
		if (username.equals(credential.getUsername())) {
			return false;
		}
		if (isCredentialValid(credential)) {
			User ourUser = userRepo.findByUsername(credential.getUsername());
			ourUser.getFollowing().add(userRepo.findByUsername(username));
			userRepo.save(ourUser);
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean unFollow(String username, Credential credential) {
		if (isCredentialValid(credential)) {
			User ourUser = userRepo.findByUsername(credential.getUsername());
			boolean unfollowed = ourUser.getFollowing().removeIf(user -> user.getUsername().equals(username));
			if (unfollowed) {
				userRepo.save(ourUser);
				return true;
			}
		}
		return false;
	}

	public Set<User> getUserFollows(String username) {
		if (getByUsername(username) != null) {
			Set<User> followProjection = userRepo.findByUsername(username).getFollowing();
			return followProjection;
		}
		return null;
	}

	public Set<User> getUserFollowers(String username) {
		if (getByUsername(username) != null) {
			return userRepo.findByUsername(username).getFollowers();
		}
		return null;
	}

	public List<Tweet> getUserTweets(String username) {
		if (getByUsername(username) != null) {
			List<Tweet> feed = userRepo.findByUsername(username).getTweets();
			Collections.reverse(feed);
			return feed;
		}
		return null;
	}

	public List<Tweet> getUserMentions(String username) {
		if (getByUsername(username) != null) {
			List<Tweet> mentions = userRepo.findByUsername(username).getMentions();
			Collections.reverse(mentions);
			return mentions;
		}
		return null;
	}
}
