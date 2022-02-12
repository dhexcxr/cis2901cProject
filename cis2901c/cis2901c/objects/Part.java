package cis2901c.objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Part extends DbObjectSearchable implements DbObjectSavable{
	private int partId = -1;
	private String partNumber;
	private String supplier;
	private String category;
	private String description;
	private String notes;
	private BigDecimal cost = new BigDecimal(0);
	private BigDecimal retail = new BigDecimal(0);
	private int onHand = -1;
	
	public Part() {
	}
	
	public Part(String searchString) {
		super.searchString = searchString;
		super.searchQuery = new String("""
				SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part  
				WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ?;""");
		super.outerSearchQueryAppendix = new String(" AND partId IN (");
		super.querySubStringIndecies[0] = 13;
		super.querySubStringIndecies[1] = 87;
	}
	
//	public Part(int partId, String partNumber, String supplier, String category, String description,
//										String notes, BigDecimal cost, BigDecimal retail, int onHand) {
//		super();
//		this.partId = partId;
//		this.partNumber = partNumber;
//		this.supplier = supplier;
//		this.category = category;
//		this.description = description;
//		this.notes = notes;
//		this.cost = cost == (null) ? BigDecimal.valueOf(0) : cost;
//		this.retail = retail == (null) ? BigDecimal.valueOf(0) : retail;
//		this.onHand = onHand;
//	}
	
	public long getDbPk() {
		return getPartId();
	}
	
	public String getPkName() {
		return "partId";
	}
	
	public String getTableName() {
		return "part";
	}
	
	public int getPartId() {
		return partId;
	}
	
	public void setPartId(int partId) {
		this.partId = partId;
	}

	public String getPartNumber() {
		return partNumber == null ? "error" : partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getSupplier() {
		return supplier == null ? "" : supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getCategory() {
		return category == null ? "" : category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description == null ? "error" : description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes == null ? "" : notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getCost() {
		return cost == null ? BigDecimal.valueOf(0) : cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost == null ? BigDecimal.valueOf(0) : cost;
	}

	public BigDecimal getRetail() {
		return retail == null ? BigDecimal.valueOf(0) : retail;
	}

	public void setRetail(BigDecimal retail) {
		this.retail = retail == null ? BigDecimal.valueOf(0) : retail;
	}

	public int getOnHand() {
		return onHand == -1 ? 0 : onHand;
	}

	public void setOnHand(int onHand) {
		this.onHand = onHand;
	}

	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter
		Map<String, String> dataMap = new HashMap<>();
		
		if (partId != -1) {
			dataMap.put("partId", Integer.toString(partId));
		}
		dataMap.put("partNumber", partNumber);
		dataMap.put("supplier", supplier);
		dataMap.put("category", category);
		dataMap.put("description", description);
		dataMap.put("notes", notes);
		dataMap.put("cost", cost.toString());
		dataMap.put("retail", retail.toString());
		dataMap.put("onHand", Integer.toString(onHand));
		
		return dataMap;
	}
	
	// START object search methods
//		public String getSearchString() {
//			return searchString;
//		}
//
//		public StringBuilder getSearchQuery() {
//			return searchQuery;
//		}
//
//		public String getOuterSearchQuery() {
//			StringBuilder outerSearchQuery = new StringBuilder(getSearchQuery());
//			outerSearchQuery.delete(outerSearchQuery.length() - 1, outerSearchQuery.length());
//			outerSearchQuery.append(outerSearchQueryAppendix);
//			return outerSearchQuery.toString();
//		}
//		
//		public int[] getQuerySubStringIndecies() {
//			return querySubStringIndecies;
//		}
		// END object search methods
}
