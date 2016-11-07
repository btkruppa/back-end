package cooksys.request_models;

import cooksys.entity.Credentials;
import cooksys.entity.Profile;

public class CreateProfileRequestModel {
	private Credentials credentials;
	private Profile profile;

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
