package cis2901c.objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class JobPart extends InvoicePart {

	// jobpart Database Table
	
	private long jobPartId = -1;
	private long partId;
	private long jobId;
	private String partNumber;
	private String description;
	private int quantity;
	private BigDecimal soldPrice = BigDecimal.valueOf(0);
	
	private Part part;
	
	private Map<String, String> dataMap = new HashMap<>();

	public JobPart() {
		this("", "", 1, BigDecimal.valueOf(0));
	}
	
	public JobPart(Part part) {
		this(-1, part.getPartId(), 0, part.getPartNumber(), part.getDescription(), 1, part.getRetail());
		this.part = part;
	}
	
	public JobPart(String partNumber, String description, int quantity,
			BigDecimal soldPrice) {
		this(-1, 0, 0, partNumber, description, quantity, soldPrice);
	}

	public JobPart(long jobPartId, long partId, long jobId, String partNumber, String description, int quantity,
			BigDecimal soldPrice) {
		super();
		this.jobPartId = jobPartId;
		this.partId = partId;
		this.jobId = jobId;
		this.partNumber = partNumber;
		this.description = description;
		this.quantity = quantity;
		this.soldPrice = soldPrice;
	}

	public JobPart(long jobId) {
		super.searchString = Long.toString(jobId);
		super.searchQuery = "SELECT jobpartid, jobid, partid, partnumber, description, quantity, soldprice FROM cis2901c.jobpart WHERE jobid = ?;";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndecies[0] = 0;
		super.querySubStringIndecies[1] = 0;
	}
	
	public long getJobPartId() {
		return jobPartId;
	}

	public void setJobPartId(long jobPartId) {
		this.jobPartId = jobPartId;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Part getPart() {
		if (part == null) {
			part = new Part();
		}
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	@Override
	public long getDbPk() {
		return getJobPartId();
	}

	@Override
	public void setDbPk(long dbPk) {
		jobPartId = dbPk;
	}

	@Override
	public String getPkName() {
		return "jobpartid";
	}

	@Override
	public String getTableName() {
		return "jobpart";
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO Auto-generated method stub
		dataMap.put("partid", Long.toString(partId));
		dataMap.put("partnumber", partNumber);
		dataMap.put("description", description);
		dataMap.put("quantity", Integer.toString(quantity));
		dataMap.put("soldprice", soldPrice.toString());
		return dataMap;
	}

}
