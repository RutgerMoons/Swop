package domain.order;


import java.util.Arrays;

import domain.car.ICarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.NotImplementedException;

/**
 * Class representing an order from a garageholder. There are 5 attributes
 * specifying a certain order : the description/type of carModel the
 * garageholder order, the name of the garageholder. Moreover the amount of cars
 * ordered and the amount of cars yet to be completed, are specific attributes
 * of an order. Each order also has an estimated time. This is an estimation of
 * when the order will be completed. This time is expressed in days and minutes.
 */
public class StandardOrder implements IOrder {

	private ICarModel description;
	private String garageholder;
	private int quantity, pendingCars;
	private UnmodifiableClock estimatedTime;
	private UnmodifiableClock orderTime;

	

	/**
	 * Constructor of an Order, given the name of the orderer, the type of
	 * carModel and the amount of cars to be ordered.
	 */
	public StandardOrder(String holder, ICarModel description, int quantity) {
		this.setDescription(description);
		this.setGarageHolder(holder);
		this.setQuantity(quantity);
		this.setPendingCars(quantity);
		UnmodifiableClock estimated = null; //TODO
		//this.setEstimatedTime(estimated);
	}

	/**
	 * Assignes the given name to the name of the garageholder.
	 */
	private void setGarageHolder(String holder) {
		if (holder == null || holder.equals(" ")) {
			throw new IllegalArgumentException();
		}
		this.garageholder = holder;
	}

	
	public String getGarageHolder() {
		return this.garageholder;
	}

	/**
	 * Changing the amount of pendingCars to the given amount. That's how other
	 * classes may check if the order is completed or not. The method checks if
	 * the given amount is lower than zero. If so an IllegalArgumentException is
	 * thrown.
	 */
	private void setPendingCars(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException();
		}
		this.pendingCars = quantity;
	}

	
	public int getPendingCars() {
		return this.pendingCars;
	}

	/**
	 * Method for assigning the amount of cars ordered to the given amount. The
	 * method checks if the given amount is lower than zero. If so an
	 * IllegalArgumentException is thrown.
	 */
	private void setQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException();
		}
		this.quantity = quantity;
	}

	
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * Assigning the type/name of the ordered carModel to the given description.
	 * The method throws an IllegalArgumentException is the given name equals
	 * null.s
	 */
	private void setDescription(ICarModel description2) {
		if (description2 == null) {
			throw new IllegalArgumentException();
		}
		this.description = description2;
	}

	
	public ICarModel getDescription() {
		return description;
	}

	public UnmodifiableClock getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(UnmodifiableClock orderTime) {
		if(orderTime == null){
			throw new IllegalArgumentException();
		}
		this.orderTime = orderTime;
	}

	public UnmodifiableClock getEstimatedTime() {
		return this.estimatedTime;
	}

	public void setEstimatedTime(UnmodifiableClock clock) {
		if (clock == null) {
			throw new IllegalArgumentException();
		}
		this.estimatedTime = clock;
	}

	public void completeCar() {
		this.setPendingCars(--pendingCars);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + description.hashCode();
		result = prime * result + garageholder.hashCode();
		result = prime * result + pendingCars;
		result = prime * result + quantity;
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
		StandardOrder other = (StandardOrder) obj;
		if (!description.equals(other.description))
			return false;
		if (!garageholder.equals(other.garageholder))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String orderInString = this.getQuantity() + " " + this.getDescription();
		if (this.getPendingCars() > 0) {
			orderInString += " Estimated completion time: "
					+ this.getEstimatedTime().getDays() + " days and "
					+ this.getEstimatedTime().getMinutes() / 60 + " hours and "
					+ this.getEstimatedTime().getMinutes() % 60 + " minutes";
		}
		return orderInString;
	}

	@Override
	public UnmodifiableClock getDeadline() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void setDeadline(UnmodifiableClock clock) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	@Override
	public int getProductionTime() {
		return this.getDescription().getTemplate().getshizzle();
	}
}
