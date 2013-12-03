package com.maestro.schoologyapp.api.menus;

import java.util.*;

import javax.ws.rs.*;

import com.sirra.server.rest.*;
import com.sirra.appcore.menus.*;
import com.sirra.appcore.sql.*;
import com.sirra.appcore.users.*;

public class Menus extends ApiBase {

	@GET
	public List<Menu> getMenus() {
		BaseUser user = new Finder().getUser();
		
		List<Menu> menus = MenuSet.getInstance().getMenus(user.getRoleMetaId());
		return menus;
	}
	
}
