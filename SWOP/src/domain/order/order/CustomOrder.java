package domain.order.order;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.order.orderVisitor.IOrderVisitor;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * A class representing a Single Task Order.
 * It consists of a CustomVehicle, the garage holder who ordered the CustomVehicle, a quantity,
 * the time when you ordered it, the deadline and the estimated time of completion.
 *
 */
public class CustomOrder implements IOrder {

	private IVehicle description;
	private String garageholder;
	private int quantity, pendingCars;
	private ImmutableClock estimatedTime;
	private ImmutableClock orderTime;
	private ImmutableClock deadline;

	/**
	 * Create a new CustomOrder.
	 * 
	 * @param 	garageholder
	 * 			The garage holder that orders the CustomVehicle
	 * 
	 * @param 	description
	 * 			The CustomVehicle that has to be created
	 * 
	 * @param 	quantity
	 * 			How many CustomVehicles that have to be produced for this order
	 * 
	 * @param 	orderTime
	 * 			The time when this order is placed
	 * 
	 * @param 	deadline
	 * 			The deadline of the order
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the the arguments is null, the garageholder is empty or the quantity is less than or equal to zero
	 */
	public CustomOrder(String garageholder, CustomVehicle description,
			int quantity, ImmutableClock orderTime,
			ImmutableClock deadline) {
		setGarageholder(garageholder);
		setDescription(description);
		setQuantity(quantity);
		setOrderTime(orderTime);
		setDeadline(deadline);
		setPendingCars(quantity);
	}

	private void setGarageholder(String garageholder){
		if(garageholder == null || garageholder.isEmpty())
			throw new IllegalArgumentException();
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

	private void setPendingCars(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException();
		}
		this.pendingCars = quantity;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	private void setQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException();
		}
		this.quantity = quantity;
	}

	@Override
	public IVehicle getDescription() {
		return description;
	}

	private void setDescription(IVehicle description) {
		if (description == null) {
			throw new IllegalArgumentException();
		}
		this.description = description;
	}

	@Override
	public ImmutableClock getDeadline() {
		return deadline;
	}

	@Override
	public void setDeadline(ImmutableClock clock) {
		if(clock == null)
			throw new IllegalArgumentException();
		this.deadline = clock;

	}

	@Override
	public ImmutableClock getEstimatedTime() {
		return estimatedTime;
	}

	@Override
	public void setEstimatedTime(ImmutableClock clock) {
		if(clock == null)
			throw new IllegalArgumentException();
		estimatedTime = clock;

	}

	@Override
	public ImmutableClock getOrderTime() {
		return orderTime;
	}

	@Override
	public void setOrderTime(ImmutableClock clock) {
		if(clock == null)
			throw new IllegalArgumentException();
		orderTime = clock;
	}

	@Override
	public void completeCar(){
		setPendingCars(--pendingCars);
	}

	@Override
	public int getProductionTime() {
		//return 60;
		int time = 0;
		for(WorkBenchType type: getTimeAtWorkBench().keySet()){
			time += getTimeAtWorkBench().get(type);
		}
		return time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+  deadline.hashCode();
		result = prime * result	+  description.hashCode();
		result = prime * result	+ ((estimatedTime == null) ? 0 : estimatedTime.hashCode());
		result = prime * result	+ garageholder.hashCode();
		result = prime * result	+  orderTime.hashCode();
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
		IOrder other = null;
		try{
			other = (IOrder) obj;
		} catch (ClassCastException e){
			return false;
		}
		if (!description.equals(other.getDescription()))
			return false;
		
		if (!deadline.equals(other.getDeadline()))
			return false;
		
		if (estimatedTime == null) {
			if (other.getEstimatedTime() != null)
				return false;
		} else if (!estimatedTime.equals(other.getEstimatedTime()))
			return false;
		if (!garageholder.equals(other.getGarageHolder()))
			return false;
		if (!orderTime.equals(other.getOrderTime()))
			return false;
		if (pendingCars != other.getPendingCars())
			return false;
		if (quantity != other.getQuantity())
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
			if(estimatedTime!=null){
				orderInString += " Estimated completion time: "
						+ this.getEstimatedTime().getDays() + " days and "
						+ this.getEstimatedTime().getMinutes() / 60 + " hours and "
						+ this.getEstimatedTime().getMinutes() % 60 + " minutes";
			}
		}
		return orderInString;
	}

	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableCollection(this.getDescription().getVehicleOptions().values());
	}

	@Override
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(this.getDescription().getTimeAtWorkBench());
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return getDescription().getVehicleSpecification();
	}

	@Override
	public void acceptVisit(IOrderVisitor visitor) {
		if(visitor==null){
			throw new IllegalArgumentException();
		}
		visitor.visit(this);
	}

}
