package car;


public abstract class CarPart {
	
	private String type;
	
	/**
	 * 
	 * @param type One of the possible types for this CarPart
	 * @effect the description of this object equals type,
	 * 			| unless type == null
	 * 			| or type is not one of the possible types for this CarPart
	 */
	public void setType(String description){
		this.type = description;  
	}
	
	/**
	 * Returns the description of this type.
	 */
	public String getType(){
		return type;
	}
	
	
}
 