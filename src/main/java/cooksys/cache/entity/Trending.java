package cooksys.cache.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import cooksys.entity.Tag;

@Entity
public class Trending {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany
	List<Tag> trendingTags;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	Date dayTrending;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Tag> getTrendingTags() {
		return trendingTags;
	}

	public void setTrendingTags(List<Tag> trendingTags) {
		this.trendingTags = trendingTags;
	}

	public Date getDayTrending() {
		return dayTrending;
	}

	public void setDayTrending(Date dayTrending) {
		this.dayTrending = dayTrending;
	}
}
