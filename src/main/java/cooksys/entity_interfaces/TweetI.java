package cooksys.entity_interfaces;

import java.util.Date;

import cooksys.entity.Tweet;
import cooksys.entity.User;

public interface TweetI {

	// public boolean isDeleted();
	//
	// public void setDeleted(boolean deleted);

	// public Set<User> getUsersLike();
	//
	// public void setUsersLike(Set<User> usersLike);

	public Long getId();

	public void setId(Long id);

	public String getContent();

	public void setContent(String content);

	// public List<Tag> getTags();
	//
	// public void setTags(List<Tag> tags);

	public Date getPosted();

	public void setPosted(Date posted);

	// public List<TweetI> getReposts();
	//
	// public void setReposts(List<TweetI> reposts);
	//
	// public List<TweetI> getReplies();
	//
	// public void setReplies(List<TweetI> replies);

	public User getAuthor();

	public void setAuthor(User author);

	public Tweet getRepostof();

	public void setRepostof(Tweet repostof);

	public Tweet getReplyto();

	public void setReplyto(Tweet replyto);
	//
	// public List<User> getUserMentions();
	//
	// public void setUserMentions(List<User> userMentions);
}
