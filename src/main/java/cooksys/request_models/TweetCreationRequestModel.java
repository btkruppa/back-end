package cooksys.request_models;

import cooksys.entity.Credential;

public class TweetCreationRequestModel {

	private String content;

	private Credential credential;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

}
