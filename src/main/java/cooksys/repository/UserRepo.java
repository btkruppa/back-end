package cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByUsername(String name);

	List<User> findByMentionsId(Long id);
}
