package cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Tweet;

public interface TweetRepo extends JpaRepository<Tweet, Long> {

	List<Tweet> findByTagsLabel(String label);

}
