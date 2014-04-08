package domain.order;

import domain.car.ICarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

public class CustomOrder implements IOrder {

	
	//TODO
	@Override
	public String getGarageHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPendingCars() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getQuantity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ICarModel getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnmodifiableClock getDeadline() throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDeadline(UnmodifiableClock clock)
			throws NotImplementedException {
		// TODO Auto-generated method stub

	}

	@Override
	public UnmodifiableClock getEstimatedTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEstimatedTime(UnmodifiableClock clock)
			throws ImmutableException {
		// TODO Auto-generated method stub

	}

	@Override
	public UnmodifiableClock getOrderTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrderTime(UnmodifiableClock clock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeCar() throws ImmutableException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public int getProductionTime() {
		return 60;
	}

}
