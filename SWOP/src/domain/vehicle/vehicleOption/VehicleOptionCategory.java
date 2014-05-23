package domain.vehicle.vehicleOption;

/**
 * An Enum representing the possible VehicleOptionCategories to which a VehicleOption can belong.
 * Keeps track of whether the VehicleOptionCategory is mandatory or optional by default.
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
			return "Seats";
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
	}, CARGO(true){
		@Override
		public String toString() {
			return "Cargo";
		}
	}, CERTIFICATION(true){
		@Override
		public String toString() {
			return "Certification";
		}
	};

	private boolean optional;

	private VehicleOptionCategory(boolean optional){
		this.optional = optional;
	}

	/**
	 * Confirms if the VehicleOptionCategory is mandatory or optional by default.
	 * @return	True if and only if the VehicleSpecification is optional by default
	 */
	public boolean isOptional() {
		return optional;
	}

	@Override
	public abstract String toString();

}
