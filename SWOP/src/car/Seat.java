package car;

/**
 * Class that represents the carpart of type Seat.
 *
 */
public class Seat extends CarPart {

	/**
	 * Constructing an Seat and immediately 
	 * choosing a certain type of Seat given the 
	 * description.
	 */
	public Seat(String description) {
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
