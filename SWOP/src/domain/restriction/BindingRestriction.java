package domain.restriction;

import domain.car.CarOption;

public class BindingRestriction{

	private CarOption chosenPart;
	private CarOption restrictedPart;
	public BindingRestriction(CarOption chosenPart, CarOption restrictedPart){
		this.chosenPart = chosenPart;
		this.restrictedPart = restrictedPart;
	}

	public CarOption getRestrictedCarPart() {
		return restrictedPart;
	}

	public CarOption getChosenCarPart(){
		return chosenPart;
	}
}
