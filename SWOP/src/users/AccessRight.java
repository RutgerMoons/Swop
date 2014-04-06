package users;

public enum AccessRight {
	ORDER("Order a car"), ADVANCE("Advance the assemblyline"), ASSEMBLE("Perform assembly tasks");
	
	private final String description;
	private AccessRight(String description) {
		this.description = description;
	}
	
	public String toString() {
		return this.description;
	}
}
	