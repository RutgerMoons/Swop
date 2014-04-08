package domain.restriction;

import domain.car.CarPart;

public class BindingRestriction{

	private CarPart chosenPart;
	private CarPart restrictedPart;
	public BindingRestriction(CarPart chosenPart, CarPart restrictedPart){
		this.chosenPart = chosenPart;
		this.restrictedPart = restrictedPart;
	}

	public CarPart getRestrictedCarPart() {
		return restrictedPart;
	}

	public CarPart getChosenCarPart(){
		return chosenPart;
	}
}
