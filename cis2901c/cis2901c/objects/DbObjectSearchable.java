package cis2901c.objects;

public class DbObjectSearchable {
	
	protected String searchString = new String();
	protected StringBuilder searchQuery = new StringBuilder();
	protected StringBuilder outerSearchQueryAppendix = new StringBuilder();
	protected int[] querySubStringIndecies = new int[2];

	
	// START object search methods
		public String getSearchString() {
			return searchString;
		}

		public StringBuilder getSearchQuery() {
			return searchQuery;
		}

		public String getOuterSearchQuery() {
			StringBuilder outerSearchQuery = new StringBuilder(getSearchQuery());
			outerSearchQuery.delete(outerSearchQuery.length() - 1, outerSearchQuery.length());
			outerSearchQuery.append(outerSearchQueryAppendix);
			return outerSearchQuery.toString();
		}
		
		public int[] getQuerySubStringIndecies() {
			return querySubStringIndecies;
		}
		// END object search methods
}
