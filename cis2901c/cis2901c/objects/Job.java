package cis2901c.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Job extends DbObjectSearchable implements DbObjectSavable {
	
	private long jobId = -1;
	private long roId;
	private	String jobName;
	private String complaints;
	private String resolution;
	private String reccomendations;
	
	private List<Part> parts;
	// TODO change this to TableLineItems, probabaly make a new Object that extends InvoiceTableLineItem so we can override dataMap and searchQuery to provide correct DB tables 
	private List<Labor> labor;
	
	private Map<String, String> dataMap = new HashMap<>();

	public Job() {
		// TODO Auto-generated constructor stub
	}
	
	public Job(long roId) {
		super.searchString = Long.toString(roId);
		super.searchQuery = "SELECT jobid, roid, name, complaint, resolution, recommendations FROM cis2901c.job WHERE roid = ?;";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndecies[0] = 0;
		super.querySubStringIndecies[1] = 0;
	}

	public Job(String jobName, String complaints, String resolution, String reccomendations, List<Part> parts,
			List<Labor> labor) {
		super();
		this.jobName = jobName;
		this.complaints = complaints;
		this.resolution = resolution;
		this.reccomendations = reccomendations;
		this.parts = parts;
		this.labor = labor;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getRoId() {
		return roId;
	}

	public void setRoId(long roId) {
		this.roId = roId;
	}

	public String getJobName() {
		return jobName == null ? "" : jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getComplaints() {
		return complaints == null ? "" : complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public String getResolution() {
		return resolution == null ? "" : resolution;
	}
	
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getReccomendations() {
		return reccomendations == null ? "" : reccomendations;
	}

	public void setReccomendations(String reccomendations) {
		this.reccomendations = reccomendations;
	}

	public List<Part> getParts() {
		if (parts == null) {
			parts = new ArrayList<>();
		}
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}
	
	public void addPart(Part part) {
		this.parts.add(part);
	}
	
	public void deletePart(Part part) {
		this.parts.remove(part);
	}

	public List<Labor> getLabor() {
		if (labor == null) {
			labor = new ArrayList<>();
		}
		return labor;
	}

	public void setLabor(List<Labor> labor) {
		this.labor = labor;
	}
	
	public void addLabor(Labor labor) {
		this.labor.add(labor);
	}
	
	public void deleteLabor(Labor labor) {
		this.labor.remove(labor);
	}

	@Override
	public long getDbPk() {
		return getJobId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		jobId = dbPk;
	}

	@Override
	public String getPkName() {
		return "jobid";
	}

	@Override
	public String getTableName() {
		return "job";
	}

	@Override
	public Map<String, String> getDataMap() {
		if (dataMap.isEmpty() ) {
			if (jobId != -1) {
				dataMap.put("jobid", Long.toString(jobId));
			}
			dataMap.put("name", jobName);
			dataMap.put("complaint", complaints);
			dataMap.put("resolution", resolution);
			dataMap.put("recommendations", reccomendations);
		}
		return dataMap;
	}

}
