package com.maestro.schoologyapp.bootstrap;

import org.eclipse.jetty.annotations.*;
import org.eclipse.jetty.plus.webapp.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;

import com.maestro.schoologyapp.api.accounts.entity.*;
import com.maestro.schoologyapp.api.schoology.*;
import com.maestro.schoologyapp.api.users.entity.*;
import com.maestro.schoologyapp.bootstrap.filters.*;
import com.sirra.server.*;
import com.sirra.server.persistence.*;
import com.sirra.server.rest.*;
import com.sirra.server.staticfiles.filter.*;
import com.sirra.appcore.email.*;
import com.sirra.appcore.firebase.*;
import com.sirra.appcore.plans.*;
import com.sirra.appcore.sql.*;
import com.sirra.appcore.util.config.*;

public class HerokuStarter {
	
	public static void main(String[] args)
	throws Exception 
	{
		System.out.println("Starting Schoology App: Lunch Count... ");
		System.out.println("Mode: " + Mode.get().name());

    	// Initialize the Config environment variables
    	InitConfig.init();
    	
    	if(args.length > 0 && args[0].equals("nohibernate")) {
    		System.out.println("Not starting hibernate.");
    	} else {
    		HibernateStarter.init("com.maestro.schoologyapp", Config.getInstance().get("PostgresPassword"));
    	}
    	
    	SqlSearch.initSharedTables("com.maestro");
    	
    	// Set base package for API classes
    	ApiServlet.setAPIPackageBase("com.maestro");
    	
    	// Initialize Finder
    	Finder.configure(Account.class, User.class);
    	
    	// Configure plans
    	Plan smallPlan = new Plan("small", "Small", 7, 9.95);
    	Plan.configure(smallPlan, smallPlan);
    	
    	// Set firebase location
    	Firebase.setFirebaseInstance("schoologyapp-lunchcount");
    	
    	// Create the control user
    	ControlUser.init();
    	
    	// Define the menus
    	ConfigureMenus.configure();
    	
    	// Read the API classes
    	ApiServlet.prepareClasses();
    	
    	// Initialize OpenSAML libraries
    	SamlLoginParent.initializeOpenSaml();
    	
    	// Add filters
    	FilterEngine.addFilter(new InternetExplorerFilter());
    	
    	EmailPerson.configure("QuickSchools Support", "support@quickschools.com");
    	
    	System.out.println("Server Configured. Now starting webserver.");
    	
    	String webPort = System.getenv("PORT");
        int port = isBlank(webPort) ? 8080 : Integer.parseInt(webPort);
        
		Server server = new Server(port);

		String wardir = "target/lunchcount-1.0/";

		WebAppContext context = new WebAppContext();
		
		context.setResourceBase(wardir);
		context.setDescriptor(wardir + "WEB-INF/web.xml");
		
		context.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(), new WebXmlConfiguration(),
				new WebInfConfiguration(), new TagLibConfiguration(),
				new PlusConfiguration(), new MetaInfConfiguration(),
				new FragmentConfiguration(), new EnvConfiguration() });

		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		
		server.setHandler(context);
		server.start();
		server.join();
		
		System.out.println("Schoology App: Lunch Count has terminated.");
	}
	
	private static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }
}
