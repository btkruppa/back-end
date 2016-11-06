package cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Tweet;

public interface TweetRepo extends JpaRepository<Tweet, Long> {

	List<Tweet> findByTagsLabel(String label);

	Tweet findByIdAndDeletedFalse(Long id);

	List<Tweet> findByDeletedFalse();

	List<Tweet> findByDeletedFalseAndReplytoId(Long id);

	List<Tweet> findByDeletedFalseAndRepostofId(Long id);

	List<Tweet> findByDeletedFalseAndAuthorUsername(String username);

}
