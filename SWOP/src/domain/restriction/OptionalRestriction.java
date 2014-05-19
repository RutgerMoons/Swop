package domain.restriction;

import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing a restriction of this kind: "If VehicleOption 'a' is chosen, 
 * VehicleOptionCategory 'b' becomes mandatory/optional".
 */
public class OptionalRestriction{
	
	private VehicleOptionCategory type;
	private Boolean optional;
	private VehicleOption part;
	private Boolean restrictedPartAlreadyChosen;

	/**
	 * Create a new OptionalRestriction between a VehicleOption and a VehicleOptionCategory.
	 */
	public OptionalRestriction(VehicleOption part, VehicleOptionCategory type, Boolean optional){
		this.type = type;
		this.optional = optional;
		this.part = part;
	}
	
	/**
	 * The VehicleOption which, if chosen, makes the VehicleOptionCategory become mandatory/optional.
	 */
	public VehicleOption getCarPart(){
		return part;
	}
	
	/**
	 * The VehicleOptionCategory that becomes mandatory/optional if the VehicleOption is chosen.
	 */
	public VehicleOptionCategory getCarPartType(){
		return type;
	}
	
	/**
	 * Check if the VehicleOptionCategory becomes optional or mandatory.
	 * 
	 * @return	True if and only if the VehicleOptionCategory becomes optional
	 */
	public boolean isOptional(){
		return optional;
	}

	/**
	 * Check if the restricted part has already been chosen by the user.
	 * 
	 * @return 	True if and only if a VehicleOption of the VehicleOptionCategory of this restriction has already been chosen
	 */
	public Boolean getRestrictedPartAlreadyChosen() {
		return restrictedPartAlreadyChosen;
	}

	/**
	 * Set if the restricted part has been chosen already.
	 * 
	 * @param 	restrictedPartAlreadyChosen
	 * 			True if a VehicleOption of the VehicleOptionCategory of this restriction has already been chosen.
	 * 			False otherwise.
	 */
	public void setRestrictedPartAlreadyChosen(
			Boolean restrictedPartAlreadyChosen) {
		this.restrictedPartAlreadyChosen = restrictedPartAlreadyChosen;
	}
	
}
