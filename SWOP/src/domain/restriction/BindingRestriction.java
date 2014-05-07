package domain.restriction;

import domain.car.VehicleOption;

/**
 * Class representing a restriction of this kind: "If CarOption 'a' is chosen, CarOption 'b' also has to be chosen."
 *
 */
public class BindingRestriction{

	private VehicleOption chosenPart;
	private VehicleOption restrictedPart;
	
	/**
	 * Create a new bindingrestriction between two CarOptions.
	 * The restrictedPart is the part that surely has to be added to the CarModel.
	 */
	public BindingRestriction(VehicleOption chosenPart, VehicleOption restrictedPart){
		this.chosenPart = chosenPart;
		this.restrictedPart = restrictedPart;
	}

	/**
	 * The CarOption that has to be chosen also, in case the other CarOption is chosen.
	 */
	public VehicleOption getRestrictedCarPart() {
		return restrictedPart;
	}

	/**
	 * 		The CarOption that, if it is chosen, makes it so that the other CarOption also has to be chosen.
	 */
	public VehicleOption getChosenCarPart(){
		return chosenPart;
	}
}
