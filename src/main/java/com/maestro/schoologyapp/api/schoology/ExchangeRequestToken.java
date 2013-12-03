package com.maestro.schoologyapp.api.schoology;

import javax.servlet.http.*;
import javax.ws.rs.*;

import com.maestro.schoologyapp.api.schoology.entity.*;
import com.maestro.schoologyapp.api.users.entity.*;
import com.sirra.appcore.sql.*;
import com.sirra.appcore.users.*;
import com.sirra.appcore.util.*;
import com.sirra.server.rest.*;
import com.sirra.server.session.*;

/**
 * After the user approves the oauth request, schoology will redirect here.
 * 
 * We then synchronously exchange the previously-retrieved request token for an access token.
 * 
 * @author aris
 */
public class ExchangeRequestToken extends ApiBase {

	@GET
	public void exchange() {
		if(true) {
			Cookie[] cookies = SirraSession.get().getRequest().getCookies();
			
			System.out.println("Cookie debug: " + cookies.length);
			for(int i=0; i<cookies.length; i++) {
				Cookie cookie = cookies[i];
				System.out.println("  " + cookie.getName() + ": " + cookie.getValue());
			}
		}
		
		String oauthToken = getParameter("oauth_token");
		System.out.println("OAuth token is " + oauthToken);
		SchoologyToken schoologyToken = new Finder().findByField(SchoologyToken.class, "key", oauthToken);
		
		System.out.println("We found schoology token for UID: " + schoologyToken.getUid());
		
		SchoologyApi schoologyApi = new SchoologyApi();
		schoologyApi.setSchoologyToken(schoologyToken);
		
		schoologyApi.call("/oauth/access_token");
		
		String response = schoologyApi.getResponse();
		System.out.println("Response is " + response);
		
		schoologyToken.setKey(schoologyApi.getToken("oauth_token"));
		schoologyToken.setSecret(schoologyApi.getToken("oauth_token_secret"));
		schoologyToken.setIsFinalToken(true);
		
		save(schoologyToken);
		
		// Great. The user authorized. Now log user in with cookies.
		SirraLogin.loginUser(schoologyToken.getUid());
		
		// Now redirect to the lunch countapp
		try {
			String location = schoologyToken.getServer() + "/lunchcount_embedded";
			System.out.println("Redirecting to lunchcount page: " + location);
			
			SirraSession.get().getResponse().sendRedirect(location);
		} catch(Exception e) {
			System.out.println("Failed to redirect after successfully swapping request token for access token: " + ExceptionUtil.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}

}