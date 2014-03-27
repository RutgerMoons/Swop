package order;

import car.CarModel;
import car.ICarModel;

public interface IOrder {

	
	public String getGarageHolder();
	
	public int getPendingCars();
	
	public int getQuantity();
	
	public ICarModel getDescription();
	
	public int[] getEstimatedTime();
	
	
	
}
