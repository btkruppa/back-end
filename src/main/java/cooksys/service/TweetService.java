package cooksys.service;

import org.springframework.stereotype.Service;

import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.repository.CredentialRepo;
import cooksys.repository.TagRepo;
import cooksys.repository.TweetRepo;

@Service
public class TweetService {

	private TweetRepo tweetRepo;
	private TagRepo tagRepo;
	private CredentialRepo credentialRepo;

	public TweetService(TweetRepo tweetRepo, TagRepo tagRepo, CredentialRepo credentialRepo) {
		this.tweetRepo = tweetRepo;
		this.tagRepo = tagRepo;
		this.credentialRepo = credentialRepo;
	}

	public void addTags(Tweet tweet, Tag Tag) {
		tweet.getTags().add(Tag);
		tweetRepo.saveAndFlush(tweet);
	}

	public Tweet get(Long id) {
		return tweetRepo.getOne(id);
	}

	public void add(Tweet tweet) {

		tweetRepo.saveAndFlush(tweet);
	}

}
