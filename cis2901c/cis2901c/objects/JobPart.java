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
	
	public JobPart(InvoicePart invoicePart) {
		this(invoicePart.getDbPk(), invoicePart.getPartId(), 0, invoicePart.getPartNumber(),
				invoicePart.getDescription(), invoicePart.getQuantity(), invoicePart.getSoldPrice());
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

	@Override
	public long getPartId() {
		return partId;
	}

	@Override
	public void setPartId(long partId) {
		this.partId = partId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	@Override
	public String getPartNumber() {
		return partNumber;
	}

	@Override
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	@Override
	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	@Override
	public Part getPart() {
		return part;
	}

	@Override
	public void setPart(Part part) {
//		if (this.part != null && this.getPartId() != part.getPartId()) { // OLD
//		if (part != null && this.getPartId() != part.getPartId()) {	// nEWER
		if (this.part == null && part != null) {
			this.partId = part.getPartId();
			this.partNumber = part.getPartNumber();
			this.description = part.getDescription();
			this.quantity = 1;
			this.soldPrice = part.getRetail();
//			this.part = part;	//nEWER
		}
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
		dataMap.put("partid", Long.toString(partId));
		dataMap.put("partnumber", partNumber);
		dataMap.put("description", description);
		dataMap.put("quantity", Integer.toString(quantity));
		dataMap.put("soldprice", soldPrice.toString());
		return dataMap;
	}

}
