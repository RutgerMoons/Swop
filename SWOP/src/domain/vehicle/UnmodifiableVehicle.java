package domain.vehicle;

import java.util.Collections;
import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;

/**
 * Create an Immutable CarModel, only the getters are accessible.
 *
 */
public class UnmodifiableVehicle implements IVehicle {

	private IVehicle model;
	
	/**
	 * Create an Immutable CarModel.
	 * 
	 * @param model
	 * 			The mutable CarModel.
	 */
	public UnmodifiableVehicle(IVehicle model){
		if(model==null)
			throw new IllegalArgumentException();
		this.model = model;
	}
	@Override
	public Map<VehicleOptionCategory, VehicleOption> getCarParts() {
		return Collections.unmodifiableMap(model.getCarParts());
	}

	@Override
	public int hashCode(){
		return model.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return model.equals(obj);
	}

	@Override
	public String toString(){
		return model.toString();
	}
	@Override
	public void addCarPart(VehicleOption part) throws AlreadyInMapException,
			UnmodifiableException {
		throw new UnmodifiableException();
		
	}
	@Override
	public Map<VehicleOption, Boolean> getForcedOptionalTypes() throws NotImplementedException {
		return Collections.unmodifiableMap(model.getForcedOptionalTypes());
	}
	@Override
	public void addForcedOptionalType(VehicleOption type, boolean bool)
			throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	@Override
	public VehicleSpecification getSpecification() throws NotImplementedException {
		return model.getSpecification();
	}
	@Override
	public void setSpecification(VehicleSpecification template)
			throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	@Override
	public int getTimeAtWorkBench() {
		return model.getTimeAtWorkBench();
	}
}
