package cooksys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id")
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Date posted;

	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	private User author;

	@ManyToMany
	@JsonIgnore
	private List<Tag> tags;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JsonIdentityReference(alwaysAsId = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Tweet repostof;

	@ManyToOne
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Tweet replyto;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "repostof")
	@JsonIgnore
	private List<Tweet> reposts;

	@OneToMany(mappedBy = "replyto")
	@JsonIgnore
	private List<Tweet> replies;

	@ManyToMany
	@JsonIgnore
	private List<User> userMentions;

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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Date getPosted() {
		return posted;
	}

	public void setPosted(Date posted) {
		this.posted = posted;
	}

	public List<Tweet> getReposts() {
		return reposts;
	}

	public void setReposts(List<Tweet> reposts) {
		this.reposts = reposts;
	}

	public List<Tweet> getReplies() {
		return replies;
	}

	public void setReplies(List<Tweet> replies) {
		this.replies = replies;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
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

	public List<User> getUserMentions() {
		return userMentions;
	}

	public void setUserMentions(List<User> userMentions) {
		this.userMentions = userMentions;
	}
}
