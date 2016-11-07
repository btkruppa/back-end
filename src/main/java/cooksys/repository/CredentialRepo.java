package cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.entity.Credentials;

public interface CredentialRepo extends JpaRepository<Credentials, Long> {

	Credentials findByUsernameIgnoringCase(String name);

	Credentials findByUsernameAndPassword(String username, String password);
}
