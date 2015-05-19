package com.lifegoals.app.entities;

public class User {
	private int id;
	private String password;
	private String name;
	private long createdDate;
	private boolean admin = false;

	public User() {

	}

	public User(int id, String password, String name, long createdDate) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.createdDate = createdDate;
	}

	public int getId() {
		return id;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

}
