package cooksys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.Credential;
import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.repository.CredentialRepo;
import cooksys.repository.TagRepo;
import cooksys.repository.TweetRepo;
import cooksys.repository.UserRepo;

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
	public void add(Tweet tweet) {
		System.out.println("here");
		System.out.println(tweet.getCredential().getUsername());
		if (isCredentialValid(tweet.getCredential())) {
			tweet.setCredential(credentialRepo.findByUsernameAndPassword(tweet.getCredential().getUsername(),
					tweet.getCredential().getPassword()));
			tweet.setAuthor(userRepo.findByCredentialUsername(tweet.getCredential().getUsername()));
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
}
