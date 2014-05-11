package domain.vehicle.vehicleOption;

/**
 * Represents the possible CarOptionCategories to which a CarOption can belong.
 * Keeps track of whether the Category is mandatory or optional by default.
 */
public enum VehicleOptionCategory {

	 BODY(false) {
		@Override
		public String toString() {
			return "Body";
		}
	}, COLOR(false) {
		@Override
		public String toString() {
			return "Color";
		}
	}, ENGINE(false) {
		@Override
		public String toString() {
			return "Engine";
		}
	}, GEARBOX(false) {
		@Override
		public String toString() {
			return "Gearbox";
		}
	}, SEATS(false) {
		@Override
		public String toString() {
			return "Seat";
		}
	}, AIRCO(true) {
		@Override
		public String toString() {
			return "Airco";
		}
	}, WHEEL(false) {
		@Override
		public String toString() {
			return "Wheel";
		}
	}, SPOILER(true) {
		@Override
		public String toString() {
			return "Spoiler";
		}
	};
	
	private boolean optional;
	
	private VehicleOptionCategory(boolean optional){
		this.optional = optional;
	}

	public boolean isOptional() {
		return optional;
	}

	@Override
	public abstract String toString();

}
