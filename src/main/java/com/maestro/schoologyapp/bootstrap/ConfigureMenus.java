package com.maestro.schoologyapp.bootstrap;

import java.util.*;

import com.sirra.appcore.menus.*;

public class ConfigureMenus {
	
	/**
	 * Called in server bootstrap code to define menus.
	 */
	public static void configure() {
		MenuSet menuSet = MenuSet.getInstance();
		
		// First, define the the menus
		menuSet.addMenu("lunchcount", "Lunch Count", "LunchCountChannel");
		menuSet.addMenu("lunchcount_embedded", "Lunch Count", "LunchCountChannel");
		menuSet.addMenu("manage", "Manage", "ManageChannel");
		menuSet.addMenu("logout", "Log Out", "LogOut");
		
		// Next, define each role, and specify the menus available to each role
		menuSet.setTargetRoles("all");
		menuSet.assignMenus("logout");
		
		menuSet.setTargetRoles("user", "admin");
		menuSet.assignMenus("lunchcount", "lunchcount_embedded");
		
		// These are the role names from Schoology
		menuSet.setTargetRoles("School Admin");
		menuSet.assignMenus("lunchcount", "lunchcount_embedded");
		
		menuSet.setTargetRoles("sirraadmin");
		menuSet.assignMenus("manage");
	}
}