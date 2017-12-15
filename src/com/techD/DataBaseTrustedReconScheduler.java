package com.techD;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import oracle.iam.platform.OIMClient;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;
import oracle.iam.reconciliation.dao.DBUtils;
import oracle.iam.scheduler.vo.TaskSupport;

public class DataBaseTrustedReconScheduler extends TaskSupport {

	@Override
	public void execute(HashMap mapAtt) throws Exception {
		
		String dBDriver =(String)mapAtt.get("DBDriver");
		String dBUrl=(String)mapAtt.get("DBUrl");
		String schemaName=(String)mapAtt.get("SchemaName");
		String schemaPassword=(String)mapAtt.get("SchemaPasword");
		String tableName=(String)mapAtt.get("Table");
		String keyColumn=(String)mapAtt.get("KeyColum");
		String OIMEmployeeType=(String)mapAtt.get("OIMEmployeeType");
		String OIMOrganizationName=(String)mapAtt.get("OIMOrganizationName");
		String OIMUserType=(String)mapAtt.get("OIMUserType");
		String resourceObject=(String)mapAtt.get("ResourceObject");
		
		ReconEvent re=new ReconEvent();
		OIMClient client=re.getConnection();
		ReconOperationsService ros=	client.getService(ReconOperationsService.class);
		
		
		DBUser dbuser= new DBUser(dBDriver, dBUrl, schemaName, schemaPassword);
		List<DBUserInfo> lUser=dbuser.getAllUsers(tableName);
		
		Iterator itr=lUser.iterator();
		
		while(itr.hasNext()) {
			DBUserInfo dbUser=(DBUserInfo) itr.next();
			HashMap reconMap= new HashMap();
			reconMap.put("User Type",OIMUserType);
			reconMap.put("Employee Type",OIMEmployeeType);
			reconMap.put("Organization",OIMOrganizationName);
			reconMap.put("Last Name", dbUser.getLastName());
			reconMap.put("First Name", dbUser.getFirstName());
			reconMap.put("Middle Name",dbUser.getMiddleName());
			reconMap.put("User Login",dbUser.getLoginid());
			reconMap.put("mailid",dbUser.getMail());
			reconMap.put("Home Phone", dbUser.getPhone());
			EventAttributes ea=new EventAttributes();
			ea.setActionDate(new java.util.Date());
			ea.setChangeType(ChangeType.REGULAR);
			ea.setEventFinished(true);
			long eventKey =ros.createReconciliationEvent(resourceObject,reconMap, ea);
		    ros.processReconciliationEvent(eventKey);
		   	ros.finishReconciliationEvent(eventKey);
	     	ros.closeReconciliationEvent(eventKey);
		}
		
		
		
	}

	@Override
	public HashMap getAttributes() {
		System.out.println();
		return null;
	}

	@Override
	public void setAttributes() {
		
	}
	
	public static void main(String[] args) throws Exception {
		
		HashMap map= new HashMap();
		map.put("DBDriver", "oracle.jdbc.driver.OracleDriver");
		map.put("DBUrl", "jdbc:oracle:thin:@localhost:1521:XE");
		map.put("SchemaName", "sys as sysdba");
		map.put("SchemaPasword", "Passw0rd");
		map.put("Table", "TRUSTEDDBUSER");
		map.put("KeyColum","USERID"); 
		
		DataBaseTrustedReconScheduler dbtrs= new DataBaseTrustedReconScheduler();
		dbtrs.execute(map);
		
		/*HashMap map1= new HashMap();
		map1.put("DBDriver", "oracle.jdbc.driver.OracleDrive1");
		map1.put("DBUrl", "jdbc:oracle:thin:@localhost:1521:orcl1");
		map1.put("SchemaName", "sys as sysdba1");
		map1.put("SchemaPasword", "Passw0rd11");
		map1.put("Table", "TrustedDBTable1");
		map1.put("KeyColum","USERID1"); 
		map1.put("OIMEmployeeType","Full-Time");
		map1.put("OIMOrganizationName","Xellerate Users");
		map1.put("OIMUserType","End-User");
		dbtrs.execute(map1);
	*/	}

}
