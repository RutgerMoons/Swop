package domain.car;

public enum CarOptionCategory {

	 BODY(false), COLOR(false), ENGINE(false), GEARBOX(false), SEATS(false), AIRCO(true), WHEEL(false), SPOILER(true);
	
	private boolean optional;
	private CarOptionCategory(boolean optional){
		this.optional = optional;
	}

	public boolean isOptional() {
		return optional;
	}

}
