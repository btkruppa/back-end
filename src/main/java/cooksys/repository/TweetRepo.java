package cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Tweet;

public interface TweetRepo extends JpaRepository<Tweet, Long> {

	List<Tweet> findByTagsLabelAndDeletedFalseOrderByPostedDesc(String label);

	Tweet findByIdAndDeletedFalse(Long id);

	List<Tweet> findByDeletedFalseOrderByPostedDesc();

	List<Tweet> findByDeletedFalseAndReplytoId(Long id);

	List<Tweet> findByDeletedFalseAndRepostofId(Long id);

	List<Tweet> findByDeletedFalseAndAuthorUsernameOrderByPostedDesc(String username);

	List<Tweet> findByDeletedFalseAndUserMentionsUsernameAndUserMentionsActiveTrueOrderByPostedDesc(String username);

	List<Tweet> findByDeletedFalseAndAuthorActiveTrueAndAuthorFollowersIdOrDeletedFalseAndAuthorIdAndAuthorActiveTrueOrderByPostedDesc(
			Long id, Long id2);
}
