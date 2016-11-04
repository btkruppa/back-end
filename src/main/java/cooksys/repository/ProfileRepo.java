package cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Profile;

public interface ProfileRepo extends JpaRepository<Profile, Long> {

	Profile findByEmailIgnoringCase(String email);
}
