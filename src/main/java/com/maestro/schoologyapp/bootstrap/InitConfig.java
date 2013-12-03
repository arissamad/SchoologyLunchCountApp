package com.maestro.schoologyapp.bootstrap;

import com.sirra.appcore.util.config.*;
import com.sirra.server.*;

/**
 * Environment variables.
 * 
 * @author aris
 */
public class InitConfig {
	
	public static void init() {
		Config config = Config.getInstance();
		
		if(Mode.get() == Mode.Development) {
			config.setPlaintextVariable("SERVER", "http://localhost:8080");
			
			config.setEncryptedVariable("PostgresPassword", "7d5a95fae0a8184cd603fb465f28aa966a0be3252aa5446affc8007c2b95b9f1");
			
			config.setPlaintextVariable("SchoologyConsumerKey", "ee4664e81b89d2ffb9756b319feba534052663b3c");
			config.setPlaintextVariable("SchoologyConsumerSecret", "416825aea7611325c2188be17ffc7873");
		} else {
			config.setPlaintextVariable("SERVER", "http://schoologyapptest.herokuapp.com");
			
			config.setEncryptedVariable("PostgresPassword", "7d5a95fae0a8184cd603fb465f28aa966a0be3252aa5446affc8007c2b95b9f1");
			
			config.setPlaintextVariable("SchoologyConsumerKey", "ee4664e81b89d2ffb9756b319feba534052663b3c");
			config.setPlaintextVariable("SchoologyConsumerSecret", "416825aea7611325c2188be17ffc7873");
		}
	}
}