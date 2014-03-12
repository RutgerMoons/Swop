package car;

/**
 * Class that represents the carpart of type Color.
 *
 */
public class Color extends CarPart {

	/**
	 * Constructing an Color and immediately 
	 * choosing a certain type of Color given the 
	 * description.
	 */
	public Color(String description){
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
