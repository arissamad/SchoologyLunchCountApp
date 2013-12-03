package com.maestro.schoologyapp.api.schoology;

import java.util.*;

import javax.ws.rs.*;

import com.sirra.api.login.Login;
import com.sirra.server.rest.*;

/**
 * This is to support 3rd party cookie described here: 
 * https://developers.schoology.com/app-platform/allowing-cookies
 * 
 * Basically, we just set a dumb cookie. But because the browser has seen that we've done that,
 * it'll allow the future cookies (or so I understand).
 * 
 * @author aris
 *
 */
public class SetCookie extends ApiBase {

	@GET
	public Map<String, Object> setCookie() {
		Login.addSessionCookie("test-setcookie-1", "123");
		
		return success();
	}
}
