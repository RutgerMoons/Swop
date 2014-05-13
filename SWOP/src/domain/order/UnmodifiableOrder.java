<<<<<<< HEAD
package domain.order;

import java.util.Collection;

import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.UnmodifiableVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Create an Immutable Order, only the getters are accessible.
 *
 */
public class UnmodifiableOrder implements IOrder {

	IOrder order;
	/**
	 * Create an Immutable Order.
	 * 
	 * @param iOrder
	 * 			The mutable Order.
	 */
	public UnmodifiableOrder(IOrder iOrder){
		if(iOrder==null)
			throw new IllegalArgumentException();
		this.order = iOrder;
	}
	
	@Override
	public String getGarageHolder() {
		return order.getGarageHolder();
	}

	@Override
	public int getPendingCars() {
		return order.getPendingCars();
	}

	@Override
	public int getQuantity() {
		return order.getQuantity();
	}

	@Override
	public IVehicle getDescription() {
		return new UnmodifiableVehicle(order.getDescription());
	}

	@Override
	public ImmutableClock getEstimatedTime() {
		return order.getEstimatedTime();
	}
	
	@Override
	public int hashCode(){
		return order.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return order.equals(obj);
	}

	@Override
	public String toString(){
		return order.toString();
	}

	@Override
	public void setEstimatedTime(ImmutableClock clock) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public void completeCar() throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public ImmutableClock getDeadline() throws NotImplementedException {
		return order.getDeadline();
	}

	@Override
	public void setDeadline(ImmutableClock clock)
			throws NotImplementedException, UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public ImmutableClock getOrderTime() {
		return order.getOrderTime();
	}

	@Override
	public void setOrderTime(ImmutableClock clock) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public int getProductionTime() {
		return order.getProductionTime();
	}
	
	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return this.order.getVehicleOptions();
	}

	@Override
	public void addToSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm, IJob job) {
		if (schedulingAlgorithm == null || job == null) {
			throw new IllegalArgumentException();
		}
		this.order.addToSchedulingAlgorithm(schedulingAlgorithm, job);
	}
	
	@Override
	public int getTimeAtWorkBench() {
		return this.order.getTimeAtWorkBench();
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return order.getVehicleSpecification();
	}
}
=======
package domain.order;

import java.util.Collection;

import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.UnmodifiableVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Create an Immutable Order, only the getters are accessible.
 *
 */
public class UnmodifiableOrder implements IOrder {

	IOrder order;
	/**
	 * Create an Immutable Order.
	 * 
	 * @param iOrder
	 * 			The mutable Order.
	 */
	public UnmodifiableOrder(IOrder iOrder){
		if(iOrder==null)
			throw new IllegalArgumentException();
		this.order = iOrder;
	}
	
	@Override
	public String getGarageHolder() {
		return order.getGarageHolder();
	}

	@Override
	public int getPendingCars() {
		return order.getPendingCars();
	}

	@Override
	public int getQuantity() {
		return order.getQuantity();
	}

	@Override
	public IVehicle getDescription() {
		return new UnmodifiableVehicle(order.getDescription());
	}

	@Override
	public ImmutableClock getEstimatedTime() {
		return order.getEstimatedTime();
	}
	
	@Override
	public int hashCode(){
		return order.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return order.equals(obj);
	}

	@Override
	public String toString(){
		return order.toString();
	}

	@Override
	public void setEstimatedTime(ImmutableClock clock) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public void completeCar() throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public ImmutableClock getDeadline() throws NotImplementedException {
		return order.getDeadline();
	}

	@Override
	public void setDeadline(ImmutableClock clock)
			throws NotImplementedException, UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public ImmutableClock getOrderTime() {
		return order.getOrderTime();
	}

	@Override
	public void setOrderTime(ImmutableClock clock) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public int getProductionTime() {
		return order.getProductionTime();
	}
	
	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return this.order.getVehicleOptions();
	}

	@Override
	public void addToSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm, IJob job) {
		if (schedulingAlgorithm == null || job == null) {
			throw new IllegalArgumentException();
		}
		this.order.addToSchedulingAlgorithm(schedulingAlgorithm, job);
	}
	
	@Override
	public int getTimeAtWorkBench() {
		return this.order.getTimeAtWorkBench();
	}
}
>>>>>>> 055a1b08e363d3b15422fcf64b26d6081bbf5699
