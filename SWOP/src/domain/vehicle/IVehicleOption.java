package domain.vehicle;

public interface IVehicleOption {
	
	/**
	 * Returns the description of this type.
	 */
	public String getDescription();
	
	/**
	 * Get the type of part this option represents.
	 */
	public VehicleOptionCategory getType();
	
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
