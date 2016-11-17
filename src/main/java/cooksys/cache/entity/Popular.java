package cooksys.cache.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import cooksys.entity.Tweet;

@Entity
public class Popular {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany
	List<Tweet> popularTweets;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	Date dayPopular;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Tweet> getPopularTweets() {
		return popularTweets;
	}

	public void setPopularTweets(List<Tweet> popularTweets) {
		this.popularTweets = popularTweets;
	}

	public Date getDayPopular() {
		return dayPopular;
	}

	public void setDayPopular(Date dayPopular) {
		this.dayPopular = dayPopular;
	}
}
