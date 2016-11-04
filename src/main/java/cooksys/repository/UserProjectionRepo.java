package cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.User;
import cooksys.projections.UserProjection;

public interface UserProjectionRepo extends JpaRepository<User, Long> {
	UserProjection findByUsername(String name);
}
