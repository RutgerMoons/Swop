package car;


public abstract class CarPart {
	
	private String description;
	
	public void setDescription(String type){
		this.description = type;  
	}
	
	public String getDescription(){
		return this.description;
	}
}
 