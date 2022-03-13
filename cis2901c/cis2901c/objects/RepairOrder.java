package cis2901c.objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairOrder extends DbObjectSearchable implements DbObjectSavable{
	private long repairOrderId;		// TODO add this Pk into DB
//	private long repairOrderNumber;
	private long customerId;
	private String customerName;
	private String customerData;
	private long unitId;
	private List<Job> jobs;
	private Timestamp createdDate;
	private Timestamp closedDate;
	private BigDecimal tax;
	private BigDecimal total;
//	private boolean cashiered;		// we might be able to use the presence of closedDate to tell if it has been cashiered
	
	
	public RepairOrder() {
	}
	
	public RepairOrder(String searchString) {
		super.searchString = searchString;
		// TODO searchQuery is preliminary
		// TODO get job names to display in search results
		super.searchQuery = """
				SELECT roId, customerId, unitId, createdTime, closedTime, cashiered FROM cis2901c.ro 
				WHERE roId LIKE ?;""";
		super.outerSearchQueryAppendix = " AND roId IN (";
		super.querySubStringIndecies[0] = 11;
		super.querySubStringIndecies[1] = 67;
	}
	
	public long getRepairOrderId() {
		return repairOrderId;
	}

	public void setRepairOrderId(long repairOrderId) {
		this.repairOrderId = repairOrderId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String firstname, String lastname) {
		if (firstname != null) {
			firstname = firstname.equals("") ? firstname : ", " + firstname;
		}
		this.customerName = lastname + firstname;
	}

	public String getCustomerData() {
		return customerData;
	}

	public void setCustomerData(String customerData) {
		this.customerData = customerData;
	}

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Timestamp closedDate) {
		this.closedDate = closedDate;
	}

	public List<Job> getJobs() {
		if (jobs == null) {
			jobs = new ArrayList<>();
		}
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	@Override
	public long getDbPk() {
		return repairOrderId;
	}
	
	@Override
	public String getPkName() {
		return "roid";
	}

	@Override
	public String getTableName() {
		return "ro";
	}
	
	public Map<String, String> getDataMap() {
		// TODO Auto-generated method stub
		Map<String, String> dataMap = new HashMap<>();
		if (repairOrderId != -1) {
			dataMap.put("roid", Long.toString(repairOrderId));
		}
		dataMap.put("customerid", Long.toString(customerId));
		dataMap.put("customerdata", customerData);
		dataMap.put("unitid", Long.toString(unitId));
		dataMap.put("createdTime", createdDate.toString());
		dataMap.put("closedTime", closedDate.toString());
		dataMap.put("tax", tax.toString());
		dataMap.put("total", total.toString());
		return dataMap;
	}
}
