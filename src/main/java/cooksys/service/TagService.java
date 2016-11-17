package cooksys.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cooksys.cache.entity.Trending;
import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.repository.TagRepo;
import cooksys.repository.TrendingRepo;
import cooksys.repository.TweetRepo;

@Service
public class TagService {

	private TweetRepo tweetRepo;
	private TagRepo tagRepo;
	private TrendingRepo trendingRepo;

	public TagService(TweetRepo tweetRepo, TagRepo tagRepo, TrendingRepo trendingRepo) {
		this.tweetRepo = tweetRepo;
		this.tagRepo = tagRepo;
		this.trendingRepo = trendingRepo;
	}

	public List<Tag> getTags() {
		return tagRepo.findAll();
	}

	@Transactional
	public List<Tag> getTrendingTags() {
		Date date = new Date(); 
		List<Trending> trending = trendingRepo
				.findByDayTrendingGreaterThanOrderByDayTrendingDesc(new Date(date.getTime() - 1 * 3600 * 1000));
		if (trending.size() >= 1) {
			List<Tag> trendingTags = trending.get(0).getTrendingTags();
			Collections.sort(trendingTags, new Comparator<Tag>() {
				@Override
				public int compare(Tag t1, Tag t2) {
					return Math.toIntExact(t2.getTweets().size()) - Math.toIntExact(t1.getTweets().size());
				}

			});
			return trendingTags;
		} else {
			Trending newTrending = new Trending();
			List<Tag> trendingTags = tagRepo
					.findByLastUsedGreaterThanOrderByLastUsedDesc(new Date(date.getTime() - (7 * 24 * 3600 * 1000)));
			Collections.sort(trendingTags, new Comparator<Tag>() {
				@Override
				public int compare(Tag t1, Tag t2) {
					return Math.toIntExact(t2.getTweets().size()) - Math.toIntExact(t1.getTweets().size());
				}

			});
			if (trendingTags.size() < 20) {
				newTrending.setTrendingTags(trendingTags.subList(0, trendingTags.size()));
				trendingRepo.saveAndFlush(newTrending);
				return trendingTags.subList(0, trendingTags.size());
			} else {
				newTrending.setTrendingTags(trendingTags.subList(0, 20));
				trendingRepo.saveAndFlush(newTrending);
				return trendingTags.subList(0, 20);
			}
		}
	}

	public List<Tweet> getTweetsByTag(String label) {
		return tweetRepo.findByTagsLabelAndDeletedFalseOrderByPostedDesc(label);
	}

}
