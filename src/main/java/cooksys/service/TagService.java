package cooksys.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.repository.TagRepo;
import cooksys.repository.TweetRepo;

@Service
public class TagService {

	private TweetRepo tweetRepo;
	private TagRepo tagRepo;

	public TagService(TweetRepo tweetRepo, TagRepo tagRepo) {
		this.tweetRepo = tweetRepo;
		this.tagRepo = tagRepo;
	}

	public List<Tag> getTags() {
		return tagRepo.findAll();
	}

	public List<Tweet> getTweetsByTag(String label) {
		return tweetRepo.findByTagsLabelAndDeletedFalseOrderByPostedDesc(label);
	}

}
