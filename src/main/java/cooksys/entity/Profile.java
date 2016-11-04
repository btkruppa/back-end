package cooksys.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String username;

	private String firstName;
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	private String phoneNumber;

	@OneToMany
	@JsonIgnore
	private List<Tweet> tweets;

	@ManyToMany
	@JsonIgnore
	private Set<Profile> following;

	@ManyToMany(mappedBy = "following")
	@JsonIgnore
	private Set<Profile> followers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public Set<Profile> getFollowing() {
		return following;
	}

	public void setFollowing(Set<Profile> following) {
		this.following = following;
	}

	public Set<Profile> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<Profile> followers) {
		this.followers = followers;
	}

	
}
