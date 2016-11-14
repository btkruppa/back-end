package cooksys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.entity.Credentials;
import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.projections.DeletedTweetProjection;
import cooksys.repository.CredentialRepo;
import cooksys.repository.TagRepo;
import cooksys.repository.TweetRepo;
import cooksys.repository.UserRepo;
import cooksys.request_models.TweetCreationRequestModel;
import cooksys.response_models.Context;

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
			} else {
				tag.setLastUsed(new Date());
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
	public Tweet add(TweetCreationRequestModel tweetRequest) throws Exception {
		if (isCredentialValid(tweetRequest.getCredentials())) {
			Tweet tweet = new Tweet();
			tweet.setContent(tweetRequest.getContent());
			tweet.setAuthor(userRepo.findByUsernameAndActiveTrue(tweetRequest.getCredentials().getUsername()));

			tweet.setTags(extractTags(tweetRequest.getContent()));
			tweet.setUserMentions(extractMentions(tweetRequest.getContent()));

			return tweetRepo.saveAndFlush(tweet);
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	public List<Tweet> getAll() {
		return tweetRepo.findByDeletedFalseOrderByPostedDesc();
	}

	private boolean isCredentialValid(Credentials credentials) {
		return (credentialRepo.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword()) == null)
				? false : true;
	}

	@Transactional
	public Tweet repost(Long id, TweetCreationRequestModel tweetCreationRequestModel) throws Exception {
		if (isCredentialValid(tweetCreationRequestModel.getCredentials())) {
			Tweet repostedTweet = getTweet(id);
			Tweet tweet = new Tweet();
			tweet.setAuthor(
					userRepo.findByUsernameAndActiveTrue(tweetCreationRequestModel.getCredentials().getUsername()));
			tweet.setRepostof(repostedTweet);
			tweet.setContent(tweetCreationRequestModel.getContent());

			tweet.setUserMentions(extractMentions(tweetCreationRequestModel.getContent()));
			tweet.setTags(extractTags(tweetCreationRequestModel.getContent()));

			tweet = tweetRepo.saveAndFlush(tweet);
			repostedTweet.getReposts().add(tweet);
			return tweet;
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	@Transactional
	public Tweet reply(Long id, TweetCreationRequestModel tweetCreationRequestModel) throws Exception {
		if (isCredentialValid(tweetCreationRequestModel.getCredentials())) {
			Tweet repliedTweet = getTweet(id);
			Tweet tweet = new Tweet();
			tweet.setAuthor(
					userRepo.findByUsernameAndActiveTrue(tweetCreationRequestModel.getCredentials().getUsername()));
			tweet.setReplyto(repliedTweet);
			tweet.setContent(tweetCreationRequestModel.getContent());

			tweet.setUserMentions(extractMentions(tweetCreationRequestModel.getContent()));
			tweet.setTags(extractTags(tweetCreationRequestModel.getContent()));

			tweet = tweetRepo.saveAndFlush(tweet);
			repliedTweet.getReplies().add(tweet);
			return tweet;
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	@Transactional
	public void like(Long id, Credentials credentials) throws Exception {
		if (isCredentialValid(credentials)) {
			User user = userRepo.findByUsernameAndActiveTrue(credentials.getUsername());
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
	public DeletedTweetProjection delete(Long id, Credentials credentials) throws Exception {
		if (isCredentialValid(credentials)) {
			Tweet tweet = getTweet(id);
			if (credentials.getUsername().equals(tweet.getAuthor().getUsername())) {

				// alright maybe there is a better way to project but because I
				// have logic in the getters and setters this is all I could
				// figure out
				DeletedTweetProjection deletedTweet = new DeletedTweetProjection(tweet);

				tweet.setDeleted(true);
				tweetRepo.save(tweet);
				return deletedTweet;
			} else {
				throw new Exception("Credentials do not match the tweets author");
			}
		} else {
			throw new Exception("Invalid Credentials");
		}
	}

	private List<Tweet> getAfter(Tweet tweet) {
		List<Tweet> after = new ArrayList<Tweet>();
		List<Tweet> replies = tweet.getReplies();
		// List<Tweet> reposts = tweet.getReposts();
		if (replies.size() != 0) {
			replies.forEach(reply -> {
				after.add(reply);
				after.addAll(getAfter(reply));
			});
		}
		// if (reposts.size() != 0) {
		// reposts.forEach(repost -> {
		// after.add(repost);
		// after.addAll(getAfter(repost));
		// });
		// }

		Collections.sort(after, new Comparator<Tweet>() {
			@Override
			public int compare(Tweet t1, Tweet t2) {
				// when I wrote this code the unix millisecond time was
				// 1478461217792 so no tweet should ever have been created
				// before that time so we can then safely convert to int for
				// comparison sake. Yes if this program runs for too long it can
				// literally break ...
				return Math.toIntExact(t1.getPosted().getTime() - 1478461217792L)
						- Math.toIntExact(t2.getPosted().getTime() - 1478461217792L);
			}

		});

		return after;
	}

	public Context getContext(Long id) throws Exception {
		Context context = new Context();
		Tweet tweet = getTweet(id);
		context.setTarget(tweet);
		List<Tweet> before = new ArrayList<Tweet>();
		Tweet repostofOrReplyto = tweet.getRepostof();
		if (repostofOrReplyto == null) {
			repostofOrReplyto = tweet.getReplyto();
		}
		while (repostofOrReplyto != null) {
			before.add(repostofOrReplyto);
			if (repostofOrReplyto.getRepostof() != null) {
				repostofOrReplyto = repostofOrReplyto.getRepostof();
			} else {
				repostofOrReplyto = repostofOrReplyto.getReplyto();
			}
		}
		context.setBefore(before);

		context.setAfter(getAfter(tweet));

		return context;
	}
}
