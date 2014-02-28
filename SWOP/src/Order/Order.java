package Order;

public class Order {

	private String description, garageholder;
	private int quantity, pendingCars, estimatedTime;

	public Order(String holder, String description, int quantity){
		this.setDescription(description);
		this.setGarageHolder(holder);
		this.setQuantity(quantity);
		this.setPendingCars(quantity);
	}

	private void setGarageHolder(String holder) {
		if(holder !=null && !holder.equals(" ")){
			this.garageholder=holder;
		}
	}

	public String getGarageHolder(){
		return this.garageholder;
	}

	private void setPendingCars(int quantity2) {
		if(quantity2 >=0){
			this.pendingCars=quantity2;
		}
	}

	private void setQuantity(int quantity) {
		if(quantity !=0){
			this.quantity=quantity;
		}
	}

	public int getQuantity(){
		return this.quantity;
	}

	private void setDescription(String description) {
		if(description != null){
			this.description=description;
		}
	}

	public String getDescription(){
		return this.description;
	}

	public int getEstimatedTime(){
		return this.estimatedTime;
	}

	public void setEstimatedTime(int time){
		this.estimatedTime = time;
	}
	
	public void CarCompleted(){
		this.setPendingCars(--pendingCars);
	}
}
