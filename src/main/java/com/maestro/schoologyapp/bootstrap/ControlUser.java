package com.maestro.schoologyapp.bootstrap;

import java.util.*;

import com.maestro.schoologyapp.api.accounts.entity.*;
import com.maestro.schoologyapp.api.users.entity.*;
import com.sirra.appcore.accounts.*;
import com.sirra.server.session.*;

/**
 * Create default account and user for Control user.
 * 
 * @author aris
 *
 */
public class ControlUser {
	
	public static void init() {
		SirraSession.start();
		SirraSession ss = SirraSession.get();
		
		Account account = (Account) ss.getHibernateSession().get(Account.class, 0);
		
		if(account == null) {
			account = new Account();
			
			account.setCreationDate(new Date());
			account.setId(-1);
			account.setName("control");
			account.setName("Control Account");
			account.setStatus(AccountStatus.Trial.name());
			
			ss.getHibernateSession().merge(account);
		}
		
		User user = (User) ss.getHibernateSession().get(User.class, "control");
		
		if(user == null) {
			user = new User();
			user.setId("control");
			user.setEmail("control");
			user.setName("Control User");
			user.setAccountId("" + account.getId());
			
			user.setRoleMetaId("sirraadmin");
			user.encryptAndSetPassword("control");
			
			ss.getHibernateSession().merge(user);
		}
		
		ss.commitButLeaveRunnning();
	}
}
