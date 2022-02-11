package cis2901c.objects;

import java.util.Date;
import java.util.Map;

public class RepairOrder extends DbObjectSearchable implements DbObjectSavable{
	long repairOrderId;		// TODO add this Pk into DB
	long repairOrderNumber;
	long customerId;
	long unitId;
	Date createdDate;
	Date closedDate;
	
	public RepairOrder() {
	}
	
	public RepairOrder(String searchString) {
		super.searchString = searchString;
		// TODO searchQuery is preliminary
		super.searchQuery = new StringBuilder("""
				SELECT roId, customerId, unitId, createdTime, closedTime, cashiered FROM cis2901c.ro 
				WHERE roId LIKE ?;""");
		super.outerSearchQueryAppendix = new StringBuilder(" AND roId IN (");
		super.querySubStringIndecies[0] = 11;
		super.querySubStringIndecies[1] = 67;
	}
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
