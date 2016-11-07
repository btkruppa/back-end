package cooksys.request_models;

import cooksys.entity.Credentials;

public class TweetCreationRequestModel {

	private String content;

	private Credentials credentials;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
