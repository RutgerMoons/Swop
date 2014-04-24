package domain.restriction;

import domain.car.CarOption;

/**
 * Class representing a restriction of this kind: "If CarOption 'a' is chosen, CarOption 'b' also has to be chosen."
 *
 */
public class BindingRestriction{

	private CarOption chosenPart;
	private CarOption restrictedPart;
	
	/**
	 * Create a new bindingrestriction between two CarOptions.
	 * The restrictedPart is the part that surely has to be added to the CarModel.
	 */
	public BindingRestriction(CarOption chosenPart, CarOption restrictedPart){
		this.chosenPart = chosenPart;
		this.restrictedPart = restrictedPart;
	}

	/**
	 * The CarOption that has to be chosen also, in case the other CarOption is chosen.
	 */
	public CarOption getRestrictedCarPart() {
		return restrictedPart;
	}

	/**
	 * 		The CarOption that, if it is chosen, makes it so that the other CarOption also has to be chosen.
	 */
	public CarOption getChosenCarPart(){
		return chosenPart;
	}
}
