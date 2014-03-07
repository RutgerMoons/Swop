package car;


public abstract class CarPart {
	
	private String description;
	
	/**
	 * 
	 * @param type One of the possible types for this CarPart
	 * @effect the description of this object equals type,
	 * 			| unless type == null
	 * 			| or type is not one of the possible types for this CarPart
	 */
	public void setDescription(String type){
		this.description = type; 
	}
	
	/**
	 * 
	 * @return The description of this object
	 */
	public String getDescription(){
		return this.description;
	}
}
 