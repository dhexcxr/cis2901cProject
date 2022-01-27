package cis2901c.objects;

import java.math.BigDecimal;

public class Part {
	private int partId = -1;
	private String partNumber;
//	private long supplierId;		// supplier table needed
//	private long categoryId;		// category table needed
	private String supplier;
	private String category;
	private String description;
	private String notes;
	private BigDecimal cost = new BigDecimal(0);
	private BigDecimal retail = new BigDecimal(0);
	private int onHand = -1;
	
	// TODO remove newPart
//	private boolean newPart = true;
	
	public Part() {
	}
	
	public Part(int partId, String partNumber, String supplier, String category, String description, String notes, BigDecimal cost,
			BigDecimal retail, int onHand) {
		super();
		this.partId = partId;
		this.partNumber = partNumber;
		this.supplier = supplier;
		this.category = category;
		this.description = description;
		this.notes = notes;
		this.cost = cost;
		this.retail = retail;
		this.onHand = onHand;
//		this.newPart = newPart;
	}
	
	public int getPartId() {
		return partId;
	}
	
	public void setPartId(int partId) {
		this.partId = partId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getRetail() {
		return retail;
	}

	public void setRetail(BigDecimal retail) {
		this.retail = retail;
	}

	public int getOnHand() {
		return onHand;
	}

	public void setOnHand(int onHand) {
		this.onHand = onHand;
	}
	
//	public boolean isNewPart() {
//		return newPart;
//	}
//	
//	public void setNewPart(boolean newPart) {
//		this.newPart = newPart;
//	}
	
	
}
