package cooksys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.request_models.TweetCreationRequestModel;
import cooksys.service.TweetService;

@RestController
@RequestMapping("tweets")
public class TweetsController {

	TweetService tweetService;

	public TweetsController(TweetService service) {
		this.tweetService = service;
	}

	@GetMapping
	public List<Tweet> get() {
		return tweetService.getAll();
	}

	@GetMapping("/{tweet}")
	public Tweet get(@PathVariable Tweet tweet) {
		return tweet;
	}

	@PostMapping
	public void add(@RequestBody TweetCreationRequestModel tweetRequest) {
		tweetService.add(tweetRequest);
	}

	@PutMapping("/{tweet}/tag/{tags}")
	public void addTags(@PathVariable Tweet tweet, @PathVariable Tag Tag) {
		tweetService.addTags(tweet, Tag);
	}

	@PostMapping("/{id}/repost")
	public void repost(@PathVariable Long id, @RequestBody TweetCreationRequestModel tweetCreationRequestModel) {
		tweetService.repost(id, tweetCreationRequestModel);
	}

	@GetMapping("/{id}/reposts")
	public List<Tweet> getReposts(@PathVariable Long id) {
		List<Tweet> reposts = tweetService.getReposts(id);

		reposts.forEach(tweet -> System.out.println(tweet.getClass()));

		return reposts;
	}

}
