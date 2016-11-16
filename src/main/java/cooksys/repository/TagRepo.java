package cooksys.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Tag;

public interface TagRepo extends JpaRepository<Tag, Long> {
	Tag findByLabel(String label);

	List<Tag> findByTweetsId(Long id);

	List<Tag> findByLastUsedGreaterThanOrderByLastUsedDesc(Date date);
}
