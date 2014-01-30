package com.maestro.schoologyapp.bootstrap.filters;

import javax.servlet.http.*;

import com.sirra.server.staticfiles.filter.*;

/**
 * As per http://developers.schoology.com/app-platform/security-policy, we need this for IE.
 * 
 * @author aris
 */
public class InternetExplorerFilter extends Filter {

	@Override
	public void process(String path, HttpServletRequest request, HttpServletResponse response) {

		if(matches(path, "lunchcount_embedded")) {
			response.setHeader("P3P", "CP=\"CAO IVDi OUR\"");	
		}
	}
}
