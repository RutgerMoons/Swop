package domain.restriction;

import domain.vehicle.vehicleOption.VehicleOption;

/**
 * A class representing a restriction of this kind: "If VehicleOption 'a' is chosen, VehicleOption 'b' also has to be chosen".
 */
public class BindingRestriction{

	private VehicleOption chosenPart;
	private VehicleOption restrictedPart;
	
	/**
	 * Create a new BindingRestriction between two VehicleOptions.
	 * The restrictedPart is the part that surely has to be added to the Vehicle.
	 * 
	 * @param	chosenPart
	 * 			The VehicleOption to which the restriction belongs to
	 * 
	 * @param	restrictedPart
	 * 			The VehicleOption that belongs to the chosenPart
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments are null
	 */
	public BindingRestriction(VehicleOption chosenPart, VehicleOption restrictedPart){
		if(chosenPart==null || restrictedPart ==null){
			throw new IllegalArgumentException();
		}
		this.chosenPart = chosenPart;
		this.restrictedPart = restrictedPart;
	}

	/**
	 * The VehicleOption that has to be chosen also, in case the other VehicleOption is chosen.
	 */
	public VehicleOption getRestrictedCarPart() {
		return restrictedPart;
	}

	/**
	 * The VehicleOption that, if it is chosen, makes it so that the other VehicleOption also has to be chosen.
	 */
	public VehicleOption getChosenCarPart(){
		return chosenPart;
	}
}
