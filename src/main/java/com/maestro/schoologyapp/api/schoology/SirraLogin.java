package com.maestro.schoologyapp.api.schoology;

import com.maestro.schoologyapp.api.users.entity.*;
import com.sirra.appcore.users.*;
import com.sirra.server.session.*;

/**
 * Once we verify the Schoology user, we can set our login cookie.
 * 
 * @author aris
 */
public class SirraLogin {

	public static void loginUser(String uid) {
		
		SirraSession ss = SirraSession.get();
		User user = (User) ss.getHibernateSession().get(User.class, uid);
		
		UserSession userSession = UserSession.newUserSession(user.getEmail(), user.getId(), user.getAccountId());
		ss.getHibernateSession().saveOrUpdate(userSession);
		
		// IE workaround http://developers.schoology.com/app-platform/security-policy
		ss.getResponse().setHeader("P3P", "CP=\"CAO IVDi OUR\"");
		
		com.sirra.api.login.Login.addSessionCookie("sirra-session-id", userSession.getSessionId());
	}
}