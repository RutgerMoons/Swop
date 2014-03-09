package car;

/**
 * Class that represents the carpart of type Body.
 *
 */
public class Body extends CarPart {
	
	/**
	 * Constructing an Body and immediately 
	 * choosing a certain type of Body given the 
	 * description.
	 */
	public Body(String description) {
		this.setType(description);
	}
	
	/**
	 *  Checks if the given description is null. If so an
	 *  IllegalArgument exception is thrown. If not, the description
	 *  is set to the given string.
	 *  
	 */
	public void setType(String description) {
		if (description == null) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setType(description);
		}
	}
	
	
	
	
}
