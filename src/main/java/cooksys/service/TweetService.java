package cooksys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
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

	public void addTags(Tweet tweet, Tag Tag) {
		tweet.getTags().add(Tag);
		tweetRepo.saveAndFlush(tweet);
	}

	public Tweet get(Long id) {
		return tweetRepo.getOne(id);
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
			mention = userRepo.findByUsername(m.group(0).substring(2));
			System.out.println(m.group(0).substring(2));
			if (mention != null) {
				System.out.println("here");
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
			tweet.setAuthor(userRepo.findByUsername(tweetRequest.getCredential().getUsername()));

			tweet.setTags(extractTags(tweetRequest.getContent()));
			tweet.setUserMentions(extractMentions(tweetRequest.getContent()));

			tweetRepo.saveAndFlush(tweet);
		}
	}

	public List<Tweet> getAll() {
		return tweetRepo.findAll();
	}

	private boolean isCredentialValid(Credential credential) {
		return (credentialRepo.findByUsernameAndPassword(credential.getUsername(), credential.getPassword()) == null)
				? false : true;
	}

	@Transactional
	public void repost(Long id, TweetCreationRequestModel tweetCreationRequestModel) {
		if (isCredentialValid(tweetCreationRequestModel.getCredential())) {
			Tweet repostedTweet = tweetRepo.getOne(id);
			if (repostedTweet != null) {
				Tweet tweet = new Tweet();
				tweet.setAuthor(userRepo.findByUsername(tweetCreationRequestModel.getCredential().getUsername()));
				tweet.setRepostof(repostedTweet);
				tweet.setContent(tweetCreationRequestModel.getContent());

				tweet.setUserMentions(extractMentions(tweetCreationRequestModel.getContent()));
				tweet.setTags(extractTags(tweetCreationRequestModel.getContent()));

				tweet = tweetRepo.saveAndFlush(tweet);
				repostedTweet.getReposts().add(tweet);
			}
		}
	}

	@Transactional
	public void reply(Long id, TweetCreationRequestModel tweetCreationRequestModel) {
		if (isCredentialValid(tweetCreationRequestModel.getCredential())) {
			Tweet repliedTweet = tweetRepo.getOne(id);
			if (repliedTweet != null) {
				Tweet tweet = new Tweet();
				tweet.setAuthor(userRepo.findByUsername(tweetCreationRequestModel.getCredential().getUsername()));
				tweet.setReplyto(repliedTweet);
				tweet.setContent(tweetCreationRequestModel.getContent());

				tweet.setUserMentions(extractMentions(tweetCreationRequestModel.getContent()));
				tweet.setTags(extractTags(tweetCreationRequestModel.getContent()));

				tweet = tweetRepo.saveAndFlush(tweet);
				repliedTweet.getReplies().add(tweet);
			}
		}
	}

	public List<Tweet> getReposts(Long id) {
		System.out.println(tweetRepo.getOne(id).getReposts().size());
		Tweet target = tweetRepo.getOne(id);

		for (Tweet t : target.getReposts()) {
			Hibernate.initialize(t);
			System.out.println(t.getClass());
		}

		return target.getReposts();
	}
}
