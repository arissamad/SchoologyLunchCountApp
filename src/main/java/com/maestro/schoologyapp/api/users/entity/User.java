package com.maestro.schoologyapp.api.users.entity;

import javax.persistence.*;

import com.sirra.appcore.users.*;

@Entity
@Table(name = "users")
public class User extends BaseUser {
	protected String firstName;
	protected String lastName;
	
	public User() {
		
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}