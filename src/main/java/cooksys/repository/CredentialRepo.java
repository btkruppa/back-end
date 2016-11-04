package cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Credential;

public interface CredentialRepo extends JpaRepository<Credential, Long> {

	Credential findByUsernameIgnoringCase(String name);

	Credential findByUsernameAndPassword(String username, String password);
}
