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
import cooksys.repository.TweetRepo;
import cooksys.repository.UserRepo;
import cooksys.request_models.CreateProfileRequestModel;

@Service
public class UserService {

	private CredentialRepo credentialRepo;
	private UserRepo userRepo;
	private ProfileRepo profileRepo;
	private TweetRepo tweetRepo;

	public UserService(UserRepo userRepo, CredentialRepo credentialRepo, ProfileRepo profileRepo, TweetRepo tweetRepo) {
		this.userRepo = userRepo;
		this.credentialRepo = credentialRepo;
		this.profileRepo = profileRepo;
		this.tweetRepo = tweetRepo;
	}

	public User getByUsername(String username) throws Exception {
		User user = userRepo.findByUsernameAndActiveTrue(username);
		if (user == null) {
			throw new Exception("User does not exist");
		} else {
			return user;
		}
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
	public User update(CreateProfileRequestModel createProfileRequestModel) throws Exception {
		if (isCredentialValid(createProfileRequestModel.getCredential())) {
			User oldUser = userRepo.findByUsername(createProfileRequestModel.getCredential().getUsername());
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
			oldUser.setActive(true);
			profileRepo.save(oldUserProfile);
			return oldUser;
		}
		throw new Exception("Invalid Credentials");
	}

	public List<User> get() {
		return userRepo.findByActiveTrue();
	}

	@Transactional
	public boolean follow(String username, Credential credential) throws Exception {
		if (username.equals(credential.getUsername())) {
			throw new Exception("Users cannot follow themselves");
		}
		if (isCredentialValid(credential)) {
			User ourUser = userRepo.findByUsernameAndActiveTrue(credential.getUsername());
			ourUser.getFollowing().add(userRepo.findByUsernameAndActiveTrue(username));
			userRepo.save(ourUser);
			return true;
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	@Transactional
	public boolean unFollow(String username, Credential credential) throws Exception {
		if (isCredentialValid(credential)) {
			User ourUser = userRepo.findByUsernameAndActiveTrue(credential.getUsername());
			boolean unfollowed = ourUser.getFollowing().removeIf(user -> user.getUsername().equals(username));
			if (unfollowed) {
				userRepo.save(ourUser);
				return true;
			}
			throw new Exception("User from credentials was not following the user provided");
		}
		throw new Exception("Invalid Credentials");
	}

	public Set<User> getUserFollows(String username) throws Exception {
		User user = getByUsername(username);
		// Set<User> follows = user.getFollowing();
		Set<User> follows = userRepo.findByFollowersIdAndActiveTrue(user.getId());
		return follows;
	}

	public Set<User> getUserFollowers(String username) throws Exception {
		User user = getByUsername(username);
		return userRepo.findByFollowingIdAndActiveTrue(user.getId());
	}

	public List<Tweet> getUserTweets(String username) throws Exception {
		// User user = getByUsername(username);
		// List<Tweet> feed = user.getTweets();
		// Collections.reverse(feed);
		// return feed;
		return tweetRepo.findByDeletedFalseAndAuthorUsername(username);
	}

	public List<Tweet> getUserMentions(String username) throws Exception {
		User user = getByUsername(username);
		List<Tweet> mentions = user.getMentions();
		Collections.reverse(mentions);
		return mentions;
	}

	public User delete(String username, Credential credential) throws Exception {
		if (isCredentialValid(credential)) {
			User user = getByUsername(credential.getUsername());
			user.setActive(false);
			userRepo.save(user);
			return user;
		}
		throw new Exception("Invalid credentials");
	}
}
