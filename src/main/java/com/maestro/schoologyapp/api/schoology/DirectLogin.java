package com.maestro.schoologyapp.api.schoology;

import java.io.*;
import java.util.*;

import javax.ws.rs.*;

import com.maestro.schoologyapp.api.schoology.support.*;
import com.sirra.appcore.users.*;
import com.sirra.server.session.*;


/**
 * Use this as the SAML ACS URL when you don't need oauth. This will simply log the user in (i.e. set login cookies)
 * based on the identified user.
 * 
 * @author aris
 */
public class DirectLogin extends SamlLoginParent {

	@POST
	public void login() {
		
		SchoologyUser schoologyUser = getSchoologyUser();
		initSirraUser(schoologyUser);
		
		// Okay, the token works. Now let's see if the user is still logged in.
		UserSession userSession = UserSession.getCurrentSession();
		
		if(userSession == null) {
			// Not logged in. Log the user in again.
			System.out.println("User not logged in. Logging in...");
			SirraLogin.loginUser(schoologyUser.getUid());
		}
		
		// Okay now redirect to app page.
		String location = getParameter("RelayState");
		System.out.println("User is logged in. Redirecting to lunchcount page: " + location);
		
		try {
			SirraSession.get().getResponse().sendRedirect(location);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}