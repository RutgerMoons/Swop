package code.car;

/**
 * Class that represents the carpart of type Gearbox.
 *
 */
public class Gearbox extends CarPart {
	
	/**
	 * Constructing an Gearbox and immediately 
	 * choosing a certain type of Gearbox given the 
	 * description.
	 */
	public Gearbox(String description) {
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
		if (description == null) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setType(description);
		}
	}
	
}
