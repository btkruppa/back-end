package cooksys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.Credential;
import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.repository.CredentialRepo;
import cooksys.repository.TagRepo;
import cooksys.repository.TweetRepo;
import cooksys.repository.UserRepo;
import cooksys.request_models.TweetCreationRequestModel;

@Service
public class TweetService {

	private TweetRepo tweetRepo;
	private TagRepo tagRepo;
	private CredentialRepo credentialRepo;
	private UserRepo userRepo;

	public TweetService(TweetRepo tweetRepo, TagRepo tagRepo, CredentialRepo credentialRepo, UserRepo userRepo) {
		this.tweetRepo = tweetRepo;
		this.tagRepo = tagRepo;
		this.credentialRepo = credentialRepo;
		this.userRepo = userRepo;
	}

	public Tweet getTweet(Long id) throws Exception {
		Tweet tweet = tweetRepo.findByIdAndDeletedFalse(id);
		if (tweet == null) {
			throw new Exception("Tweet does not exist");
		} else {
			return tweet;
		}
	}

	private List<Tag> extractTags(String content) {
		String pattern = "( #)\\w+([^\\s]+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(content);
		Tag tag;
		List<Tag> tags = new ArrayList<Tag>();
		while (m.find()) {
			tag = tagRepo.findByLabel(m.group(0).substring(1));
			if (tag == null) {
				tag = new Tag();
				tag.setLabel(m.group(0).substring(1));
			}
			tag = tagRepo.saveAndFlush(tag);
			tags.add(tag);
		}
		return tags;
	}

	private List<User> extractMentions(String content) {
		String pattern = "( @)\\w+([^\\s]+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(content);
		List<User> mentions = new ArrayList<User>();
		User mention;
		while (m.find()) {
			mention = userRepo.findByUsernameAndActiveTrue(m.group(0).substring(2));
			if (mention != null) {
				mentions.add(mention);
			}
		}
		return mentions;
	}

	@Transactional
	public void add(TweetCreationRequestModel tweetRequest) {
		if (isCredentialValid(tweetRequest.getCredential())) {
			Tweet tweet = new Tweet();
			tweet.setContent(tweetRequest.getContent());
			tweet.setAuthor(userRepo.findByUsernameAndActiveTrue(tweetRequest.getCredential().getUsername()));

			tweet.setTags(extractTags(tweetRequest.getContent()));
			tweet.setUserMentions(extractMentions(tweetRequest.getContent()));

			tweetRepo.saveAndFlush(tweet);
		}
	}

	public List<Tweet> getAll() {
		return tweetRepo.findByDeletedFalse();
	}

	private boolean isCredentialValid(Credential credential) {
		return (credentialRepo.findByUsernameAndPassword(credential.getUsername(), credential.getPassword()) == null)
				? false : true;
	}

	@Transactional
	public void repost(Long id, TweetCreationRequestModel tweetCreationRequestModel) throws Exception {
		if (isCredentialValid(tweetCreationRequestModel.getCredential())) {
			Tweet repostedTweet = getTweet(id);
			Tweet tweet = new Tweet();
			tweet.setAuthor(
					userRepo.findByUsernameAndActiveTrue(tweetCreationRequestModel.getCredential().getUsername()));
			tweet.setRepostof(repostedTweet);
			tweet.setContent(tweetCreationRequestModel.getContent());

			tweet.setUserMentions(extractMentions(tweetCreationRequestModel.getContent()));
			tweet.setTags(extractTags(tweetCreationRequestModel.getContent()));

			tweet = tweetRepo.saveAndFlush(tweet);
			repostedTweet.getReposts().add(tweet);
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	@Transactional
	public void reply(Long id, TweetCreationRequestModel tweetCreationRequestModel) throws Exception {
		if (isCredentialValid(tweetCreationRequestModel.getCredential())) {
			Tweet repliedTweet = getTweet(id);
			Tweet tweet = new Tweet();
			tweet.setAuthor(
					userRepo.findByUsernameAndActiveTrue(tweetCreationRequestModel.getCredential().getUsername()));
			tweet.setReplyto(repliedTweet);
			tweet.setContent(tweetCreationRequestModel.getContent());

			tweet.setUserMentions(extractMentions(tweetCreationRequestModel.getContent()));
			tweet.setTags(extractTags(tweetCreationRequestModel.getContent()));

			tweet = tweetRepo.saveAndFlush(tweet);
			repliedTweet.getReplies().add(tweet);
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	@Transactional
	public void like(Long id, Credential credential) throws Exception {
		if (isCredentialValid(credential)) {
			User user = userRepo.findByUsernameAndActiveTrue(credential.getUsername());
			if (user == null) {
				throw new Exception("User from credentials does not exist");
			} else {
				Tweet tweet = getTweet(id);
				tweet.getUsersLike().add(user);
				tweetRepo.save(tweet);
			}
		}
	}

	public List<Tweet> getReposts(Long id) throws Exception {
		// Tweet target = getTweet(id);
		//
		// for (Tweet t : target.getReposts()) {
		// Hibernate.initialize(t);
		// }
		//
		// return target.getReposts();
		getTweet(id);
		return tweetRepo.findByDeletedFalseAndRepostofId(id);
	}

	public List<Tweet> getReplies(Long id) throws Exception {
		getTweet(id); // this will throw an exception if the tweet does not
						// exist
		return tweetRepo.findByDeletedFalseAndReplytoId(id);
	}

	public Set<User> getLikes(Long id) throws Exception {
		getTweet(id); // this will throw an exception if the tweet does not
						// exist
		return userRepo.findByActiveTrueAndLikesIdAndLikesDeletedFalse(id);
	}

	public List<Tag> getTweetTags(Long id) {
		return tagRepo.findByTweetsId(id);
	}

	public List<User> getTweetMentions(Long id) {
		return userRepo.findByMentionsId(id);
	}

	@Transactional
	public Tweet delete(Long id, Credential credential) throws Exception {
		if (isCredentialValid(credential)) {
			Tweet tweet = getTweet(id);
			if (credential.getUsername().equals(tweet.getAuthor().getUsername())) {
				tweet.setDeleted(true);
				tweetRepo.save(tweet);
				return tweet;
			} else {
				throw new Exception("Credentials do not match the tweets author");
			}
		} else {
			throw new Exception("Invalid Credentials");
		}
	}
}
