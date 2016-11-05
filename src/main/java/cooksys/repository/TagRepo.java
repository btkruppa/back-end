package cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Tag;

public interface TagRepo extends JpaRepository<Tag, Long> {
	Tag findByLabel(String label);

	List<Tag> findByTweetsId(Long id);
}
