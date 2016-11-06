package cooksys.projections;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import cooksys.entity.Tweet;
import cooksys.entity.User;

public class DeletedTweetProjection {
	private Long id;
	private String content;
	private Date posted;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private User author;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Tweet repostof;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Tweet replyto;

	public DeletedTweetProjection(Tweet tweet) {
		id = tweet.getId();
		content = tweet.getContent();
		posted = tweet.getPosted();
		author = tweet.getAuthor();
		repostof = tweet.getRepostof();
		replyto = tweet.getReplyto();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPosted() {
		return posted;
	}

	public void setPosted(Date posted) {
		this.posted = posted;
	}

	public Tweet getRepostof() {
		return repostof;
	}

	public void setRepostof(Tweet repostof) {
		this.repostof = repostof;
	}

	public Tweet getReplyto() {
		return replyto;
	}

	public void setReplyto(Tweet replyto) {
		this.replyto = replyto;
	}

	public User getAuthor() {
		if (!author.isActive()) {
			return null;
		}
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
