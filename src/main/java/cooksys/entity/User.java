package cooksys.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false)
	@JsonIgnore
	private Long id;

	@OneToOne
	private Profile profile;

	@Column(unique = true, nullable = false, updatable = false)
	private String username;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Date joined = new Date();

	@ManyToMany
	@JsonIgnore
	private Set<User> following;

	@ManyToMany(mappedBy = "following")
	@JsonIgnore
	private Set<User> followers;

	@OneToMany(mappedBy = "author")
	@JsonIgnore
	private List<Tweet> tweets;

	@ManyToMany(mappedBy = "userMentions")
	@JsonIgnore
	private List<Tweet> mentions;

	@ManyToMany(mappedBy = "usersLike")
	@JsonIgnore
	private List<Tweet> likes;

	public List<Tweet> getLikes() {
		return likes;
	}

	public void setLikes(List<Tweet> likes) {
		this.likes = likes;
	}

	@JsonIgnore
	private boolean active = true;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<User> getFollowing() {
		return following;
	}

	public void setFollowing(Set<User> following) {
		this.following = following;
	}

	public Set<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public List<Tweet> getMentions() {
		return mentions;
	}

	public void setMentions(List<Tweet> mentions) {
		this.mentions = mentions;
	}
}
