package order;

import java.util.Arrays;

public class Order {

	private String description, garageholder;
	private int quantity, pendingCars;
	int[] estimatedTime;

	public Order(String holder, String description, int quantity){
		this.setDescription(description);
		this.setGarageHolder(holder);
		this.setQuantity(quantity);
		this.setPendingCars(quantity);
	}

	private void setGarageHolder(String holder) {
		if(holder == null  || holder.equals(" ")){
			throw new IllegalArgumentException();
		}
		this.garageholder=holder;
	}

	public String getGarageHolder(){
		return this.garageholder;
	}

	private void setPendingCars(int quantity2) {
		if(quantity2 <0){
			throw new IllegalArgumentException();
		}
		this.pendingCars=quantity2;
	}

	public int getPendingCars(){
		return this.pendingCars;
	}

	private void setQuantity(int quantity) {
		if(quantity <=0){
			throw new IllegalArgumentException();
		}
		this.quantity=quantity;
	}

	public int getQuantity(){
		return this.quantity;
	}

	private void setDescription(String description) {
		if(description == null || description.equals(" ")){
			throw new IllegalArgumentException();
		}
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}

	/**
	 * Get the estimated time untill completion.
	 * @return
	 * 			An array of 2 integers, with the first the days untill completion (1 is tomorrow), 
	 * 			the second gives the minutes till completion from the beginning of that day. (the amount of minutes that has to be spended on the car that day). 
	 */
	public int[] getEstimatedTime(){
		return this.estimatedTime;
	}

	/**
	 * Set the estimated time, the first integer gives the days from today before it's finished (so 1 is tomorrow);
	 * the second integer gives the minutes till completion for that day from the beginning of that day. (the amount of minutes that has to be spended on the car that day).
	 * @param array
	 */
	public void setEstimatedTime(int[] array){
		if(array==null || array.length!=2 || array[0] <0 || array[1] <0){
			throw new IllegalArgumentException(); 
		}
		this.estimatedTime = array;
	}

	public void completeCar(){
		this.setPendingCars(--pendingCars);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (!description.equals(other.description))
			return false;
		if (!garageholder.equals(other.garageholder))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}
}
