package cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Tag;

public interface TagRepo extends JpaRepository<Tag, Long> {
	Tag findByLabel(String label);
}
