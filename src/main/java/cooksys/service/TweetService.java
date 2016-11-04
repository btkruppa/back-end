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

	@Transactional
	public void add(TweetCreationRequestModel tweetRequest) {
		if (isCredentialValid(tweetRequest.getCredential())) {
			Tweet tweet = new Tweet();
			tweet.setContent(tweetRequest.getContent());
			tweet.setAuthor(userRepo.findByUsername(tweetRequest.getCredential().getUsername()));

			// for tags and users
			// ( #)\w+([^\s]+)
			String content = tweetRequest.getContent();
			String pattern = "( #)\\w+([^\\s]+)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(content);
			Tag tag;
			List<Tag> tags = new ArrayList<Tag>();
			while (m.find()) {
				System.out.println("Found value: " + m.group(0));
				tag = tagRepo.findByLabel(m.group(0).substring(1));
				if (tag == null) {
					System.out.println("here");
					tag = new Tag();
					tag.setLabel(m.group(0).substring(1));
				}
				System.out.println(tag.getLabel());
				tag = tagRepo.saveAndFlush(tag);
				System.out.println(tag.getId());
				tags.add(tag);
			}
			tweet.setTags(tags);
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
				tweet = tweetRepo.saveAndFlush(tweet);
				repostedTweet.getReposts().add(tweet);
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
