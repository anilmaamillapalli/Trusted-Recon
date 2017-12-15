package com.techD;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginException;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcEventDataReceivedException;
import Thor.API.Exceptions.tcEventNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Exceptions.tcUserNotFoundException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;

public class ReconEvent {
	
	
	public OIMClient getConnection() {
		
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
        env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, "t3://192.168.68.69:14000");        //Update localhost with your OIM machine IP
         
        System.setProperty("java.security.auth.login.config", "D:/CustomClient/config/authwl.conf");   //Update path of authwl.conf file according to your environment
        System.setProperty("OIM.AppServerType", "wls");  
        System.setProperty("APPSERVER_TYPE", "wls");
        oracle.iam.platform.OIMClient oimClient = new oracle.iam.platform.OIMClient(env);
 
         try {                        
               oimClient.login("xelsysadm", "Passw0rd1".toCharArray());         //Update password of Admin with your environment password
               System.out.print("Successfully Connected with OIM ");
             } catch (LoginException e) {
               System.out.print("Login Exception"+ e);
            }            
		
		return oimClient;
		
	}
	
	public Long genReconEvent() throws tcAPIException, tcEventNotFoundException, tcEventDataReceivedException {
		ReconEvent re= new ReconEvent();
		OIMClient client=re.getConnection();
		
	ReconOperationsService ros=	client.getService(ReconOperationsService.class);
	HashMap reconMap= new HashMap();
	reconMap.put("User Type","End-User");
	reconMap.put("Employee Type","Full-Time");
	reconMap.put("Organization","Xellerate Users");
	reconMap.put("Last Name", "kiran");
	reconMap.put("First Name", "kumar");
	reconMap.put("Middle Name","reddy");
	reconMap.put("User Login","012345");
	reconMap.put("mailid","sun123@GMAIL.COM");
	//reconMap.put("ITRESOURCEDB","TrustedITR");
	
	EventAttributes ea=new EventAttributes();
	ea.setActionDate(new java.util.Date());
	ea.setChangeType(ChangeType.REGULAR);
	ea.setEventFinished(true);
	
	
	
	long eventKey =ros.createReconciliationEvent("TrstuedDB",reconMap, ea);
      
            ros.processReconciliationEvent(eventKey);
		
	     	ros.finishReconciliationEvent(eventKey);
	     	ros.closeReconciliationEvent(eventKey);
	     	
return eventKey;
	}
	
	public boolean isUserExit(String userId) throws UserSearchException, AccessDeniedException {
		ReconEvent re= new ReconEvent();
		OIMClient client=re.getConnection();
		UserManager userMgr=client.getService(UserManager.class);
		SearchCriteria sc= new SearchCriteria("User Login",userId,SearchCriteria.Operator.EQUAL);
		Set retAttrs= new HashSet();
		retAttrs.add("Role");
		retAttrs.add("Last Name");
		
		List<User> lUser=userMgr.search(sc, retAttrs, null);
		
		for(User usr:lUser) {
			System.out.println(usr.getAttributes());
		}
		
		UserManager userMgr1=client.getService(UserManager.class);
		SearchCriteria sc1= new SearchCriteria("User Login",userId,SearchCriteria.Operator.EQUAL);
		Set retAttrs1= new HashSet();
		retAttrs1.add("act_key");
		retAttrs1.add("Display Name");
		List<User> lUser1=userMgr.search(sc1, retAttrs1, null);
		for(User usr1:lUser1) {
		System.out.println(usr1.getAttributes());
			
		}

		return false;
		
	}
	
	
	public Map getProcessInstanceKey() throws tcAPIException, tcUserNotFoundException, tcColumnNotFoundException{
		ReconEvent re= new ReconEvent();
		OIMClient client=re.getConnection();
		
		
		tcUserOperationsIntf userOperationsIntf=client.getService(tcUserOperationsIntf.class);
		Long userKey=1013L;
		
		Map<String,Long> resources= new HashMap<String,Long>();
		
	Thor.API.tcResultSet trs= userOperationsIntf.getObjects(userKey);
	
	if(trs != null) {
		int count= trs.getRowCount();
		for(int i=0; i < count;i++) {
			trs.goToRow(i);
			String objectName=trs.getStringValue("Objects.Name");
			String status=trs.getStringValue("Objects.Object Status.Status");
			
			if(status.equalsIgnoreCase("Provisioned") ||status.equalsIgnoreCase("Enabled")) {
				System.out.println("hello");
				resources.put(objectName, trs.getLongValue("Process Instance.Key"));
			}
		}
	}
		System.out.println("resources-->"+resources);
		return resources;
	}
	
	
	public Map  getUserDataFromProcessForm(long pKey) throws tcAPIException, tcNotAtomicProcessException, tcFormNotFoundException, tcProcessNotFoundException, tcColumnNotFoundException {
		ReconEvent re= new ReconEvent();
		OIMClient client=re.getConnection();
		tcFormInstanceOperationsIntf formInstanceIntf=client.getService(tcFormInstanceOperationsIntf.class);
		HashMap recontoken= new HashMap();
		tcResultSet trs= formInstanceIntf.getProcessFormData(pKey);
		int count= trs.getRowCount();
		
	     for(int i=0;i<count;i++) {
	    	 trs.goToRow(i);
	    	 String columnNames[]=trs.getColumnNames();
	    	 for(String value: columnNames) {
	    		 
	    			 System.out.println(value+"-"+trs.getStringValue(value));
	    			 recontoken.put(value, trs.getStringValue(value));
	    		
	    	 }
	     }
		
		return recontoken;
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws tcAPIException, tcEventNotFoundException, tcEventDataReceivedException, UserSearchException, AccessDeniedException, tcUserNotFoundException, tcColumnNotFoundException, tcNotAtomicProcessException, tcFormNotFoundException, tcProcessNotFoundException {
		
				ReconEvent re= new ReconEvent();
			//	re.isUserExit("USER.10");
			//	Map m=re.getProcessInstanceKey();
			//	Long processKey=(Long)m.get("LDAP User");
			//	re.getUserDataFromProcessForm(processKey);
			//	System.out.println(processKey);
				System.out.println(re.genReconEvent());
				
	}

}
