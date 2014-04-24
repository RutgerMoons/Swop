package domain.users;

public enum AccessRight {
	ORDER("Order a car"), ADVANCE("Advance the assemblyline"), ASSEMBLE("Perform assembly tasks"), 
		SHOWDETAILS("Show order details"), CUSTOMORDER("Place a custom order"), CHECKLINE("Check assembly line status"), STATISTICS("Check production statistics");

	private final String description;

	private AccessRight(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
