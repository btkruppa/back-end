package cooksys.request_models;

import cooksys.entity.Credential;
import cooksys.entity.Profile;

public class CreateProfileRequestModel {
	private Credential credential;
	private Profile profile;

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
