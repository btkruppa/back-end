package cooksys.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	Set<User> findByFollowingIdAndActiveTrue(Long id);

	User findByUsernameAndActiveTrue(String name);

	List<User> findByMentionsId(Long id);

	List<User> findByActiveTrue();

	User findByUsername(String username);

	Set<User> findByFollowersIdAndActiveTrue(Long id);

	Set<User> findByActiveTrueAndLikesIdAndLikesDeletedFalse(Long id);

	Object findByUsernameIgnoringCase(String username);

	User findByActiveTrueAndUsernameAndFollowingUsername(String username, String username2);
}
