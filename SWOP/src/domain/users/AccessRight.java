package domain.users;

/**
 * A class representing an access right that might be necessary to perform a use case.
 */
public enum AccessRight {
	ORDER("Order a car"), ASSEMBLE("Perform assembly tasks"), 
		SHOWDETAILS("Show order details"), CUSTOMORDER("Place a custom order"), 
		CHECKLINE("Check assembly line status"), STATISTICS("Check production statistics"),
		SWITCH_SCHEDULING_ALGORITHM("Switch the scheduling algorithm"),
		SWITCH_OPERATIONAL_STATUS("Switch the operational status");

	private final String description;

	private AccessRight(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
