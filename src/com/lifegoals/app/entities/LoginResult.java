package com.lifegoals.app.entities;

public class LoginResult {
	/* true if login successfull */
	private boolean success;
	/* a token that will be used for the requests later */
	private Token token;
	private int tokenId;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

 

}
