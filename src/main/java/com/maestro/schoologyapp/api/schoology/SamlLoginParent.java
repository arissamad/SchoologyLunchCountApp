package com.maestro.schoologyapp.api.schoology;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.xml.parsers.*;

import org.apache.commons.codec.binary.*;
import org.opensaml.*;
import org.opensaml.saml2.core.*;
import org.opensaml.xml.*;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.*;
import org.opensaml.xml.signature.*;
import org.w3c.dom.*;

import com.maestro.schoologyapp.api.accounts.entity.*;
import com.maestro.schoologyapp.api.schoology.support.*;
import com.maestro.schoologyapp.api.users.entity.*;
import com.sirra.appcore.accounts.*;
import com.sirra.appcore.sql.*;
import com.sirra.appcore.util.*;
import com.sirra.server.rest.*;
import com.sirra.server.session.*;

/**
 * Superclass for both Login methods (with oauth and without oauth).
 * 
 * @author aris
 */
public abstract class SamlLoginParent extends ApiBase {
	
	public static void initializeOpenSaml() {
		try {
			DefaultBootstrap.bootstrap();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected SchoologyUser getSchoologyUser() {

		String samlResponse = getParameter("SAMLResponse");
		//System.out.println("SAML: " + samlResponse);
		
		SchoologyUser schoologyUser = null;
		
		// STEP 1: The user is identified
		if(samlResponse != null && samlResponse.length() > 0) {
			
			try {
				System.out.println("Decode begin");
				Date beginDate = new Date();
				byte[] samlBytes = Base64.decodeBase64(samlResponse.getBytes());
				String samlStr = new String(samlBytes, "UTF-8");
				
				System.out.println("C1");
				
				//System.out.println("SAML Decoded:\n" + samlStr);
				//System.out.println("\n");
				
				System.out.println("C2");
				
				ByteArrayInputStream is = new ByteArrayInputStream(samlStr.getBytes());
				
				System.out.println("C3");

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);
				DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

				System.out.println("C4");
				
				Document document = docBuilder.parse(is);
				
				Date endDate = new Date();
				long ms = endDate.getTime() - beginDate.getTime();
				System.out.println("XML Document parsed in " + ms + " ms.");
				
				Element element = document.getDocumentElement();
				
				
				UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
				Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
				XMLObject responseXmlObj = unmarshaller.unmarshall(element);
				
				Response response = (Response) responseXmlObj;
				
				List<Assertion> assertions = response.getAssertions();
				
				System.out.println("Num of assertions: " + assertions.size());
				
				Assertion assertion = assertions.get(0);
				
				String subject = assertion.getSubject().getNameID().getValue();
				String issuer = assertion.getIssuer().getValue();
				
				String audience = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI();
				String statusCode = response.getStatus().getStatusCode().getValue();
				
				System.out.println("Subject: " + subject);
				System.out.println("Issuer: " + issuer);
				System.out.println("Audience: " + audience);
				System.out.println("Status code: " + statusCode);
				
				Signature sig = response.getSignature();
				
				System.out.println("Sig: " + sig);
				
				List<Attribute> attributes = assertion.getAttributeStatements().get(0).getAttributes();
				
				Map<String, String> attributeMap = new HashMap();
				
				for(Attribute attribute: attributes) {
					String name = attribute.getName();
					String value = attribute.getAttributeValues().get(0).getDOM().getTextContent();
					
					System.out.println("Found attribute " + name);
					System.out.println("  Value: " + value);
					
					attributeMap.put(name, value);
				}
				
				schoologyUser = new SchoologyUser(attributeMap);
				
				System.out.println("Got Schoology User!");
				//SignatureValidator validator = new SignatureValidator(credential);
				//validator.validate(sig);
				
			} catch(Exception e) {
				System.out.println("Error: " + ExceptionUtil.getStackTrace(e));
			}		
		}
		
		if(schoologyUser == null) {
			Map<String, String> testAttributeMap = new HashMap();
			
			testAttributeMap.put("school_nid", "66864031");
			testAttributeMap.put("school_title", "QuickSchools, Inc.");
			testAttributeMap.put("uid", "8527441");
			testAttributeMap.put("name_display", "Aris Samad-Yahaya");
			testAttributeMap.put("name_first", "Aris");
			testAttributeMap.put("name_last", "Samad-Yahaya");
			testAttributeMap.put("is_admin", "1");
			testAttributeMap.put("role_id", "260337");
			testAttributeMap.put("role_name", "School Admin");
			testAttributeMap.put("timezone_name", "America/Los_Angeles");
			testAttributeMap.put("domain", "schoology.schoology.com");

			schoologyUser = new SchoologyUser(testAttributeMap);
		}
		
		return schoologyUser;
	}
	
	protected void initSirraUser(SchoologyUser schoologyUser) {
		// First, check account
		Finder finder = new Finder();
		Account account = finder.findByField(Account.class, "schoologyAccountId", schoologyUser.getSchoolId());
		
		if(account == null) {
			System.out.println("Creating new account.");
			account = new Account();
			account.setId(new Integer(schoologyUser.getSchoolId()));
			account.setSchoologyAccountId(schoologyUser.getSchoolId());
			account.setCreationDate(new Date());
			account.setName(schoologyUser.getSchoolName());
			
			account.setPlanId("small");
			account.setStatus(AccountStatus.Trial.name());
			
			SirraSession.get().getHibernateSession().merge(account);
		}
		
		// Next check user
		User user = get(User.class, schoologyUser.getUid());
		
		if(user == null) {
			System.out.println("Creating new user.");
			user = new User();
			user.setAccountId(account.getId() + "");
			user.setId(schoologyUser.getUid());
			
			user.setEmail(schoologyUser.getUid());
			user.setFirstName(schoologyUser.getFirstName());
			user.setLastName(schoologyUser.getLastName());
			user.setName(schoologyUser.getFullName());
			user.setRoleMetaId(schoologyUser.getRoleName());
			
			SirraSession.get().getHibernateSession().merge(user);
		}
	}
	
	protected String getServer() {
		HttpServletRequest request = SirraSession.get().getRequest();
		String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort(); 
		
		return server;
	}
}
