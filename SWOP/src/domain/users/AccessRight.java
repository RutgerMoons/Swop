package domain.users;

public enum AccessRight {
	ORDER("Order a car"), ADVANCE("Advance the assemblyline"), ASSEMBLE("Perform assembly tasks");
	
	private final String description;
	private AccessRight(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return this.description;
	}
}
	