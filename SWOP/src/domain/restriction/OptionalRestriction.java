package domain.restriction;

import domain.car.CarOption;
import domain.car.CarOptionCategory;

/**
 * Class representing a restriction of this kind: "If CarOption 'a' is chosen, CarOptionCategory 'b' becomes mandatory/optional."
 *
 */
public class OptionalRestriction{
	
	private CarOptionCategory type;
	private Boolean optional;
	private CarOption part;
	private Boolean restrictedPartAlreadyChosen;

	/**
	 * Create a new OptionalRestriction between a CarOption and a CarOptionCategory.
	 */
	public OptionalRestriction(CarOption part, CarOptionCategory type, Boolean optional){
		this.type = type;
		this.optional = optional;
		this.part = part;
	}
	
	/**
	 * The CarOption which, if chosen, makes the CarOptionCategory become mandatory/optional.
	 */
	public CarOption getCarPart(){
		return part;
	}
	
	/**
	 * 		The CarOptionCategory that becomes mandatory/optional if the CarOption is chosen.
	 */
	public CarOptionCategory getCarPartType(){
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
