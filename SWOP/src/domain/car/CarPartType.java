package domain.car;

public enum CarPartType {

	AIRCO(true), BODY(false), COLOR(false), ENGINE(false), GEARBOX(false), SEATS(false), SPOILER(true), WHEEL(false);
	
	private boolean optional;
	private CarPartType(boolean optional){
		this.optional = optional;
	}

	public boolean isOptional() {
		return optional;
	}

}
