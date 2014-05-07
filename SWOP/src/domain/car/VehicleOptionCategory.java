package domain.car;

/**
 * Represents the possible CarOptionCategories to which a CarOption can belong.
 * Keeps track of whether the Category is mandatory or optional by default.
 */
public enum VehicleOptionCategory {

	 BODY(false), COLOR(false), ENGINE(false), GEARBOX(false), SEATS(false), AIRCO(true), WHEEL(false), SPOILER(true);
	
	private boolean optional;
	private VehicleOptionCategory(boolean optional){
		this.optional = optional;
	}

	public boolean isOptional() {
		return optional;
	}

}
