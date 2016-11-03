package cooksys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.service.TweetService;

@RestController
@RequestMapping("tweets")
public class TweetsController {

	TweetService tweetService;

	public TweetsController(TweetService service) {
		this.tweetService = service;
	}

	@GetMapping("/{tweet}")
	public Tweet get(@PathVariable Tweet tweet) {
		return tweet;
	}

	@PostMapping
	public void add(@RequestBody Tweet tweet) {
		tweetService.add(tweet);
	}

	@PutMapping("/{tweet}/tag/{tags}")
	public void addTags(@PathVariable Tweet tweet, @PathVariable Tag Tag) {
		tweetService.addTags(tweet, Tag);
	}
}
