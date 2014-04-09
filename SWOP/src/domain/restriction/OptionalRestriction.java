package domain.restriction;

import domain.car.CarOption;
import domain.car.CarOptionCategory;


public class OptionalRestriction{
	
	private CarOptionCategory type;
	private Boolean optional;
	private CarOption part;
	private Boolean restrictedPartAlreadyChosen;

	public OptionalRestriction(CarOption part, CarOptionCategory type, Boolean optional){
		this.type = type;
		this.optional = optional;
		this.part = part;
	}
	
	public CarOption getCarPart(){
		return part;
	}
	
	public CarOptionCategory getCarPartType(){
		return type;
	}
	
	public boolean isOptional(){
		return optional;
	}

	public Boolean getRestrictedPartAlreadyChosen() {
		return restrictedPartAlreadyChosen;
	}

	public void setRestrictedPartAlreadyChosen(
			Boolean restrictedPartAlreadyChosen) {
		this.restrictedPartAlreadyChosen = restrictedPartAlreadyChosen;
	}
	
}
