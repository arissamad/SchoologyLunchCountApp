package com.maestro.schoologyapp.api.schoology;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.xml.parsers.*;

import org.apache.commons.codec.binary.*;
import org.apache.http.*;
import org.apache.http.client.utils.*;
import org.opensaml.*;
import org.opensaml.saml2.core.*;
import org.opensaml.xml.*;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.*;
import org.opensaml.xml.signature.*;
import org.w3c.dom.*;

import com.maestro.schoologyapp.api.accounts.entity.*;
import com.maestro.schoologyapp.api.schoology.entity.*;
import com.maestro.schoologyapp.api.schoology.support.*;
import com.maestro.schoologyapp.api.users.entity.*;
import com.sirra.appcore.accounts.*;
import com.sirra.appcore.plans.*;
import com.sirra.appcore.sql.*;
import com.sirra.appcore.users.*;
import com.sirra.appcore.util.*;
import com.sirra.server.rest.*;
import com.sirra.server.session.*;

/**
 * This is the SAML ACS URL that is hit by Schoology when the user first loads the app.
 * This page is then hit again when the user approves the oauth request.
 * 
 * @author aris
 */
public class Login extends SamlLoginParent {

	@POST
	public void login() {
		
		SchoologyUser schoologyUser = getSchoologyUser();
		SchoologyToken schoologyToken = get(SchoologyToken.class, schoologyUser.getUid());
	
		// STEP 2: A synchronous request is made for a request token
		if(schoologyToken == null) {
			System.out.println("Requesting new schoology access token.");
			
			SchoologyApi schoologyApi = new SchoologyApi();
			schoologyApi.call("/oauth/request_token");
			String response = schoologyApi.getResponse();
			
			schoologyToken = new SchoologyToken();
			
			schoologyToken.setUid(schoologyUser.getUid());
			schoologyToken.setKey(schoologyApi.getToken("oauth_token"));
			schoologyToken.setSecret(schoologyApi.getToken("oauth_token_secret"));
			
			String server = getServer();
            
			schoologyToken.setServer(server);
			
			save(schoologyToken);
		}
		
		// STEP 3: Create our own account/user
		initSirraUser(schoologyUser);
		
		// STEP 4: The user is redirected to the oauth approval page
		if(schoologyToken.getIsFinalToken() == false) {
			// Still need to convert request token to access token.

			StringBuffer location = new StringBuffer();
			
			location.append("http://" + schoologyUser.getDomain() + "/oauth/authorize?");
			location.append("oauth_token=" + schoologyToken.getKey());
			location.append("&return_url=" + schoologyToken.getServer() + "/api/schoology/exchangerequesttoken");
			
			System.out.println("Redirecting to oauth authorize page: " + location.toString());
			
			try {
				SirraSession.get().getResponse().sendRedirect(location.toString());
			} catch(Exception e) {
				System.out.println("Failed to redirect to oauth authorize page: " + ExceptionUtil.getStackTrace(e));
				throw new RuntimeException(e);
			}
		}
		
		if(schoologyToken.getIsFinalToken()) {
			getUsers(schoologyUser, schoologyToken);
			
			// Okay, the token works. Now let's see if the user is still logged in.
			UserSession userSession = UserSession.getCurrentSession();
			
			if(userSession == null) {
				// Not logged in. Log the user in again.
				SirraLogin.loginUser(schoologyToken.getUid());
			}
			
			// Okay now redirect to app page.
			String location = schoologyToken.getServer() + "/lunchcount_embedded";
			System.out.println("User is already logged in. Redirecting to lunchcount page: " + location);
			
			try {
				SirraSession.get().getResponse().sendRedirect(location);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected void getUsers(SchoologyUser schoologyUser, SchoologyToken schoologyToken) {
		SchoologyApi schoologyApi = new SchoologyApi();
		schoologyApi.setSchoologyToken(schoologyToken);
		
		schoologyApi.call("/users/" + schoologyUser.getUid() + "/sections");
		
		System.out.println("Response is " + schoologyApi.getResponse());
	}
	
}
