package order;

import java.util.Arrays;

import car.ICarModel;

/**
 * Class representing an order from a garageholder. There are 5 attributes
 * specifying a certain order : the description/type of carModel the
 * garageholder order, the name of the garageholder. Moreover the amount of cars
 * ordered and the amount of cars yet to be completed, are specific attributes
 * of an order. Each order also has an estimated time. This is an estimation of
 * when the order will be completed. This time is expressed in days and minutes.
 */
public class Order implements IOrder {

	private ICarModel description;
	private String garageholder;
	private int quantity, pendingCars;
	int[] estimatedTime;

	/**
	 * Constructor of an Order, given the name of the orderer, the type of
	 * carModel and the amount of cars to be ordered.
	 */
	public Order(String holder, ICarModel description, int quantity) {
		this.setDescription(description);
		this.setGarageHolder(holder);
		this.setQuantity(quantity);
		this.setPendingCars(quantity);
		int[] estimated = {0,0};
		this.setEstimatedTime(estimated);
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


	public int[] getEstimatedTime() {
		return this.estimatedTime;
	}

	public void setEstimatedTime(int[] array) {
		if (array == null || array.length != 2 || array[0] < 0 || array[1] < 0) {
			throw new IllegalArgumentException();
		}
		this.estimatedTime = array;
	}

	public void completeCar() {
		this.setPendingCars(--pendingCars);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + description.hashCode();
		result = prime * result + Arrays.hashCode(estimatedTime);
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
		Order other = (Order) obj;
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
					+ this.getEstimatedTime()[0] + " days and "
					+ this.getEstimatedTime()[1] / 60 + " hours and "
					+ this.getEstimatedTime()[1] % 60 + " minutes";
		}
		return orderInString;
	}
}
