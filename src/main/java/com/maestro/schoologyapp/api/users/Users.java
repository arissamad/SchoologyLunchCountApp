package com.maestro.schoologyapp.api.users;

import java.util.*;

import javax.ws.rs.*;

import com.maestro.schoologyapp.api.users.entity.*;
import com.sirra.appcore.sql.*;
import com.sirra.server.rest.*;
import com.sirra.server.rest.annotations.*;

public class Users extends ApiBase {

	@GET
	@Parameters({"accountId"})
	public List<User> getUsers(String accountId) {
		SqlParams sqlParams = getSqlParams();
		
		sqlParams.addConstraint("users.accountId = '" + accountId + "'");
		
		List<User> users = SqlSearch.search(User.class, "SELECT ${columns} FROM users", sqlParams);
		return users;	
	}
	
	@GET
	@BY_ID
	public User getUser(String userId) {
		User user = get(User.class, userId);
		return user;
	}
	
	@POST
	@BY_ID
	@Parameters({"userId", "accountId", "firstName", "lastName", "name", "email", "password", "roleMetaId"})
	public void updateUser(String userId, String accountId, 
			String firstName, String lastName, String name, String email, String password, String roleMetaId)
	{
		User user = get(User.class, userId);
		
		if(user == null) {
			user = new User();
			user.setAccountId(accountId);
			user.setRoleMetaId("user");
		}
		
		if(firstName != null) user.setFirstName(firstName);
		if(lastName != null) user.setLastName(lastName);
		if(user != null) user.setName(name);
		if(email != null) user.setEmail(email);
		
		if(password != null) user.encryptAndSetPassword(password);
		if(roleMetaId != null) user.setRoleMetaId(roleMetaId);
		
		save(user);
	}
	
	@DELETE
	@BY_ID
	public void deleteUser(String userId)
	{
		User user = get(User.class, userId);
		delete(user);
	}
}
