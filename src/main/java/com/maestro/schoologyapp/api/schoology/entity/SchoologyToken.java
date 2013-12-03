package com.maestro.schoologyapp.api.schoology.entity;

import javax.persistence.*;

/**
 * This is the OAuth access token for a specific schoology user.
 * 
 * @author aris
 */
@Entity
@Table(name = "SchoologyTokens")
public class SchoologyToken {

	protected String uid;
	
	protected String key;
	protected String secret;
	
	protected boolean isFinalToken;
	
	/*
	 * This is like "http://app.herokuapp.com/". This allows the flow to work easily between test and production
	 * environments.
	 */
	protected String server;
	
	public SchoologyToken() {
		isFinalToken = false;
	}
	
	@Id
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public boolean getIsFinalToken() {
		return isFinalToken;
	}

	public void setIsFinalToken(boolean isFinalToken) {
		this.isFinalToken = isFinalToken;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}
