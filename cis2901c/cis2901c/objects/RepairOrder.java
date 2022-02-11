package cis2901c.objects;

import java.util.Date;
import java.util.Map;

public class RepairOrder implements DbObjectSavable{
	long repairOrderId;		// TODO add this Pk into DB
	long repairOrderNumber;
	long customerId;
	long unitId;
	Date createdDate;
	Date closedDate;

	@Override
	public long getDbPk() {
		return repairOrderId;
	}
	
	@Override
	public String getPkName() {
		return "repairOrderId";
	}

	@Override
	public String getTableName() {
		return "ro";
	}
	
	public Map<String, String> getDataMap() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
