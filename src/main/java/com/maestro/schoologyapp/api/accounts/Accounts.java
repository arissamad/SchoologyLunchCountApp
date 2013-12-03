package com.maestro.schoologyapp.api.accounts;

import java.util.*;

import javax.ws.rs.*;

import com.maestro.schoologyapp.api.accounts.entity.*;
import com.sirra.appcore.accounts.*;
import com.sirra.appcore.sql.*;
import com.sirra.server.rest.*;
import com.sirra.server.rest.annotations.*;

public class Accounts extends ApiBase {

	@GET
	public List<Account> getAccounts() {
		SqlParams sqlParams = getSqlParams();
		
		List<Account> accounts = SqlSearch.search(Account.class, "SELECT ${columns} FROM accounts", sqlParams);
		return accounts;	
	}
	
	@GET
	@BY_ID
	public Account getAccount(String accountId) {
		return get(Account.class, new Integer(accountId));
	}
	
	@POST
	@BY_ID
	@Parameters({"accountId", "name"})
	public void updateAccount(String accountId, String name) {
		Account account;
		
		if(accountId != null) {
			account = get(Account.class, new Integer(accountId));
		} else {
			account = new Account();
			account.setCreationDate(new Date());
		}
		
		if(name != null) account.setName(name);
		
		save(account);
	}
	
	@DELETE
	public void deleteAccount(String accountId) {
		Account account = get(Account.class, accountId);
		delete(account);
	}
}