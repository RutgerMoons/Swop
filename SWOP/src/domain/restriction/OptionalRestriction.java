package domain.restriction;

import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;

/**
 * Class representing a restriction of this kind: "If CarOption 'a' is chosen, CarOptionCategory 'b' becomes mandatory/optional."
 *
 */
public class OptionalRestriction{
	
	private VehicleOptionCategory type;
	private Boolean optional;
	private VehicleOption part;
	private Boolean restrictedPartAlreadyChosen;

	/**
	 * Create a new OptionalRestriction between a CarOption and a CarOptionCategory.
	 */
	public OptionalRestriction(VehicleOption part, VehicleOptionCategory type, Boolean optional){
		this.type = type;
		this.optional = optional;
		this.part = part;
	}
	
	/**
	 * The CarOption which, if chosen, makes the CarOptionCategory become mandatory/optional.
	 */
	public VehicleOption getCarPart(){
		return part;
	}
	
	/**
	 * 		The CarOptionCategory that becomes mandatory/optional if the CarOption is chosen.
	 */
	public VehicleOptionCategory getCarPartType(){
		return type;
	}
	
	/**
	 * True if the CarOptionCategory becomes optional.
	 * False if the CarOptionCategory becomes mandatory.
	 */
	public boolean isOptional(){
		return optional;
	}

	/**
	 * True if a CarOption of the CarOptionCategory of this restriction has already been chosen.
	 * False otherwise.
	 */
	public Boolean getRestrictedPartAlreadyChosen() {
		return restrictedPartAlreadyChosen;
	}

	/**
	 * Set if the restrictedparts is already chosen.
	 * @param restrictedPartAlreadyChosen
	 * 			True if a CarOption of the CarOptionCategory of this restriction has already been chosen.
	 * 			False otherwise.
	 */
	public void setRestrictedPartAlreadyChosen(
			Boolean restrictedPartAlreadyChosen) {
		this.restrictedPartAlreadyChosen = restrictedPartAlreadyChosen;
	}
	
}
