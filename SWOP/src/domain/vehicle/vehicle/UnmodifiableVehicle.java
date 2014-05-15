package domain.vehicle.vehicle;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import domain.assembly.workBench.WorkbenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

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
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableMap(model.getVehicleOptions());
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
	public void addCarPart(VehicleOption part) throws AlreadyInMapException {
		throw new UnmodifiableException();
		
	}
	@Override
	public Map<VehicleOption, Boolean> getForcedOptionalTypes() {
		return Collections.unmodifiableMap(model.getForcedOptionalTypes());
	}
	@Override
	public void addForcedOptionalType(VehicleOption type, boolean bool)
			throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	@Override
	public VehicleSpecification getSpecification() {
		return model.getSpecification();
	}
	@Override
	public void setSpecification(VehicleSpecification template)
			throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	@Override
	public Map<WorkbenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(model.getTimeAtWorkBench());
	}
	@Override
	public boolean canBeHandled(Set<VehicleSpecification> responsibilities) {
		return model.canBeHandled(responsibilities);
	}
}
