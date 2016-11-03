package cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
