package cooksys.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.Credentials;
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

	public boolean isUsernamveValid(String username) {
		return (credentialRepo.findByUsernameIgnoringCase(username) == null) ? true : false;
	}

	public boolean doesProfileExist(String username) {
		return (profileRepo.findByEmailIgnoringCase(username) == null) ? false : true;
	}

	private boolean isCredentialValid(Credentials credentials) {
		return (credentialRepo.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword()) == null)
				? false : true;
	}

	@Transactional
	public User add(CreateProfileRequestModel createProfileRequestModel) throws Exception {
		if (isUsernamveValid(createProfileRequestModel.getCredentials().getUsername())) {
			if (!doesProfileExist(createProfileRequestModel.getProfile().getEmail())) {
				User user = new User();
				user.setUsername(createProfileRequestModel.getCredentials().getUsername());
				user.setProfile(createProfileRequestModel.getProfile());
				credentialRepo.saveAndFlush(createProfileRequestModel.getCredentials());
				profileRepo.saveAndFlush(createProfileRequestModel.getProfile());
				return userRepo.saveAndFlush(user);
			}
			throw new Exception("Email address is already in use");
		} else {
			if (isCredentialValid(createProfileRequestModel.getCredentials())) {
				User deletedUser = userRepo.findByUsername(createProfileRequestModel.getCredentials().getUsername());
				if (deletedUser != null) {
					if (deletedUser.isActive()) {
						throw new Exception("You already have an active account with those credentials");
					}
					deletedUser.setActive(true);
					return userRepo.save(deletedUser);
				}
			}

		}
		throw new Exception("User already exists");
	}

	@Transactional
	public User update(CreateProfileRequestModel createProfileRequestModel) throws Exception {
		if (isCredentialValid(createProfileRequestModel.getCredentials())) {
			User oldUser = userRepo.findByUsername(createProfileRequestModel.getCredentials().getUsername());
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
	public boolean follow(String username, Credentials credentials) throws Exception {
		if (username.equals(credentials.getUsername())) {
			throw new Exception("Users cannot follow themselves");
		}
		if (isCredentialValid(credentials)) {
			User ourUser = getByUsername(credentials.getUsername());
			User alreadyFollows = userRepo.findByActiveTrueAndUsernameAndFollowingUsername(credentials.getUsername(),
					username);
			if (alreadyFollows != null) {
				throw new Exception(credentials.getUsername() + " already follows " + username);
			}
			ourUser.getFollowing().add(getByUsername(username));
			userRepo.save(ourUser);
			return true;
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	@Transactional
	public boolean unFollow(String username, Credentials credentials) throws Exception {
		if (isCredentialValid(credentials)) {
			User ourUser = getByUsername(credentials.getUsername());
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
		User user = getByUsername(username); // this will throw an error if the
												// user does
		Set<User> follows = userRepo.findByFollowersIdAndActiveTrue(user.getId());
		return follows;
	}

	public Set<User> getUserFollowers(String username) throws Exception {
		User user = getByUsername(username);
		return userRepo.findByFollowingIdAndActiveTrue(user.getId());
	}

	public List<Tweet> getUserTweets(String username) throws Exception {
		return tweetRepo.findByDeletedFalseAndAuthorUsernameOrderByPostedDesc(username);
	}

	public List<Tweet> getUserMentions(String username) throws Exception {
		getByUsername(username); // this will throw an error if the user does
									// not exist or is not active
		return tweetRepo.findByDeletedFalseAndUserMentionsUsernameAndUserMentionsActiveTrueOrderByPostedDesc(username);
	}

	public User delete(String username, Credentials credentials) throws Exception {
		if (isCredentialValid(credentials)) {
			User user = getByUsername(credentials.getUsername());
			user.setActive(false);
			userRepo.save(user);
			return user;
		}
		throw new Exception("Invalid credentials");
	}

	public List<Tweet> getUserFeed(String username) throws Exception {
		User user = getByUsername(username);

		return tweetRepo
				.findByDeletedFalseAndAuthorActiveTrueAndAuthorFollowersIdOrDeletedFalseAndAuthorIdAndAuthorActiveTrueOrderByPostedDesc(
						user.getId(), user.getId());
	}
}
