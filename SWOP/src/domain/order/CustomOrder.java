package domain.order;

import domain.car.ICarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

public class CustomOrder implements IOrder {

	private ICarModel description;
	private String garageholder;
	private int quantity, pendingCars;
	private UnmodifiableClock estimatedTime;
	private UnmodifiableClock orderTime;
	private UnmodifiableClock deadline;

	public CustomOrder(String garageholder, ICarModel description,
			int quantity, UnmodifiableClock orderTime,
			UnmodifiableClock deadline) {
		setGarageholder(garageholder);
		this.description = description;
		this.quantity = quantity;
		this.orderTime = orderTime;
		this.deadline = deadline;
	}

	private void setGarageholder(String garageholder){
		this.garageholder = garageholder;
	}
	@Override
	public String getGarageHolder() {
		return garageholder;
	}

	@Override
	public int getPendingCars() {
		return pendingCars;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public ICarModel getDescription() {
		return description;
	}

	@Override
	public UnmodifiableClock getDeadline() {
		return deadline;
	}

	@Override
	public void setDeadline(UnmodifiableClock clock) {
		this.deadline = clock;

	}

	@Override
	public UnmodifiableClock getEstimatedTime() {
		return estimatedTime;
	}

	@Override
	public void setEstimatedTime(UnmodifiableClock clock) {
		estimatedTime = clock;

	}

	@Override
	public UnmodifiableClock getOrderTime() {
		return orderTime;
	}

	@Override
	public void setOrderTime(UnmodifiableClock clock) {
		orderTime = clock;
	}

	@Override
	public void completeCar() throws ImmutableException {
		pendingCars--;
	}

	@Override
	public int getProductionTime() {
		return 60;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deadline == null) ? 0 : deadline.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((estimatedTime == null) ? 0 : estimatedTime.hashCode());
		result = prime * result
				+ ((garageholder == null) ? 0 : garageholder.hashCode());
		result = prime * result
				+ ((orderTime == null) ? 0 : orderTime.hashCode());
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
		CustomOrder other = (CustomOrder) obj;
		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (estimatedTime == null) {
			if (other.estimatedTime != null)
				return false;
		} else if (!estimatedTime.equals(other.estimatedTime))
			return false;
		if (garageholder == null) {
			if (other.garageholder != null)
				return false;
		} else if (!garageholder.equals(other.garageholder))
			return false;
		if (orderTime == null) {
			if (other.orderTime != null)
				return false;
		} else if (!orderTime.equals(other.orderTime))
			return false;
		if (pendingCars != other.pendingCars)
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String line = System.lineSeparator();
		String orderInString = this.getQuantity() + " " + this.getDescription() + line;
		if (this.getPendingCars() > 0) {
			orderInString+= "Deadline: "
					+ this.getDeadline().getDays() + " days and "
					+ this.getDeadline().getMinutes() / 60 + " hours and "
					+ this.getDeadline().getMinutes() % 60 + " minutes"
					+ line;
			orderInString += " Estimated completion time: "
					+ this.getEstimatedTime().getDays() + " days and "
					+ this.getEstimatedTime().getMinutes() / 60 + " hours and "
					+ this.getEstimatedTime().getMinutes() % 60 + " minutes";
		}
		return orderInString;
	}
	
}
