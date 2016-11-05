package cooksys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.entity.User;
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

	@GetMapping("/{id}/tags")
	public List<Tag> getTweetsTags(@PathVariable Long id) {
		return tweetService.getTweetTags(id);
	}

	@GetMapping("/{id}/mentions")
	public List<User> getTweetsMentions(@PathVariable Long id) {
		return tweetService.getTweetMentions(id);
	}

	@PostMapping
	public void add(@RequestBody TweetCreationRequestModel tweetRequest) {
		tweetService.add(tweetRequest);
	}

	@PostMapping("/{id}/repost")
	public void repost(@PathVariable Long id, @RequestBody TweetCreationRequestModel tweetCreationRequestModel) {
		tweetService.repost(id, tweetCreationRequestModel);
	}

	@PostMapping("/{id}/reply")
	public void reply(@PathVariable Long id, @RequestBody TweetCreationRequestModel tweetCreationRequestModel) {
		tweetService.reply(id, tweetCreationRequestModel);
	}

	@GetMapping("/{id}/reposts")
	public List<Tweet> getReposts(@PathVariable Long id) {
		List<Tweet> reposts = tweetService.getReposts(id);
		return reposts;
	}

	@GetMapping("/{id}/replies")
	public List<Tweet> getReplies(@PathVariable Long id) {
		return tweetService.getReplies(id);
	}

}
