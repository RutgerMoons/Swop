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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarPart other = (CarPart) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
 