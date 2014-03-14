package code.car;

/**
 * Class that represents the carpart of type Wheel.
 *
 */
public class Wheel extends CarPart {

	/**
	 * Constructing an Wheel and immediately choosing a certain type of Wheel
	 * given the description.
	 */
	public Wheel(String description) {
		this.setType(description);
	}

	/**
	 *  Checks if the given description is null. If so an
	 *  IllegalArgument exception is thrown. If not, the description
	 *  is set to the given string.
	 *  
	 */
	@Override
	public void setType(String description) {
		if (description == null ){//|| !possibleWheels.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setType(description);
		}
	}

}
