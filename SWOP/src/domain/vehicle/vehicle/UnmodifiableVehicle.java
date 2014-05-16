package domain.vehicle.vehicle;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import domain.assembly.workBench.WorkbenchType;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * Create an unmodifiable Vehicle, only the getters are accessible.
 *
 */
public class UnmodifiableVehicle implements IVehicle {

	private IVehicle model;
	
	/**
	 * Create an unmodifiable Vehicle.
	 * 
	 * @param	model
	 * 			The mutable Vehicle.
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
	public void addCarPart(VehicleOption part){
		throw new UnmodifiableException();
		
	}
	@Override
	public Map<VehicleOption, Boolean> getForcedOptionalTypes() {
		return Collections.unmodifiableMap(model.getForcedOptionalTypes());
	}
	@Override
	public void addForcedOptionalType(VehicleOption type, boolean bool){
		throw new UnmodifiableException();
	}
	@Override
	public VehicleSpecification getSpecification() {
		return model.getSpecification();
	}
	@Override
	public void setSpecification(VehicleSpecification template){
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
