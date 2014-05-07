package domain.vehicle;

import domain.exception.UnmodifiableException;

public interface IVehicleOption {

	
	/**
	 * 
	 * @param description
	 *            The type of this carpart.
	 * @throws UnmodifiableException 
	 * @post the description of this object equals type, | unless type == null |
	 *       or type is not one of the possible types for this CarPart
	 * @throws IllegalArgumentException
	 *             if description==null or isEmpty
	 */
	public void setDescription(String description) throws UnmodifiableException;
	
	/**
	 * Returns the description of this type.
	 */
	public String getDescription();
	
	/**
	 * Get the type of part this option represents.
	 */
	public VehicleOptionCategory getType();

	/**
	 * Set the type this option represents.
	 * @throws UnmodifiableException 
	 * 		
	 */
	public void setType(VehicleOptionCategory type) throws UnmodifiableException;
	
	/**
	 * Get the string representation of what the description of the task looks like.
	 * @return
	 */
	public String getTaskDescription();
	
	/**
	 * Get the string representation of what the description of the task looks like.
	 * @return
	 */
	public String getActionDescription();
	
	
}
