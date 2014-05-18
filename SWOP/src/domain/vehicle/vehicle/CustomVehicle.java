package domain.vehicle.vehicle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing a set of VehicleOptions, which can be used with CustomOrders. 
 * It implements the interface IVehicle.  
 */
public class CustomVehicle implements IVehicle {

	private HashMap<VehicleOptionCategory, VehicleOption> vehicleOptions;
	private Map<WorkBenchType, Integer> timeAtWorkbench;
	private VehicleSpecification specification;
	/**
	 * Create a new CustomVehicle. 
	 */
	public CustomVehicle(){
		vehicleOptions = new HashMap<>();
		timeAtWorkbench = new HashMap<>();
		Map<WorkBenchType, Integer> timeAtWorkBench = new HashMap<WorkBenchType, Integer>();
		for(WorkBenchType type: WorkBenchType.values()){
			if(!type.equals(WorkBenchType.CARGO) && !type.equals(WorkBenchType.CERTIFICATION)){
				timeAtWorkBench.put(type, 60);
			}
		}
		this.timeAtWorkbench = timeAtWorkBench;
		specification = new VehicleSpecification("custom", new HashSet<VehicleOption>(), timeAtWorkBench);

	}

	@Override
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableMap(vehicleOptions);
	}

	@Override
	public void addVehicleOption(VehicleOption part) throws AlreadyInMapException {
		if(part==null){
			throw new IllegalArgumentException();
		}
		if(vehicleOptions.containsKey(part.getType())){
			throw new AlreadyInMapException();
		}
		vehicleOptions.put(part.getType(), part);
	}

	@Override
	public Map<VehicleOption, Boolean> getForcedOptionalTypes() {
		throw new NotImplementedException();
	}

	@Override
	public void addForcedOptionalType(VehicleOption type, boolean bool){
		throw new NotImplementedException();
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return specification;
	}

	@Override
	public void setVehicleSpecification(VehicleSpecification template) {
		if(template==null){
			throw new IllegalArgumentException();
		}
		this.specification = template;
	}

	@Override
	public String toString(){
		String line = System.lineSeparator();
		String result = "";
		for(VehicleOptionCategory type: vehicleOptions.keySet()){
			VehicleOption option = vehicleOptions.get(type);
			result+= line + type.toString() + ": " +option.getDescription();
		}
		result = result.replaceFirst(line, "");
		return result;
	}

	@Override
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(timeAtWorkbench);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ specification.hashCode();
		result = prime * result
				+ timeAtWorkbench.hashCode();
		result = prime * result
				+ vehicleOptions.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IVehicle other = null;
		try{
			other = (IVehicle) obj;
		} catch (ClassCastException e){
			return false;
		}
		if (!specification.equals(other.getVehicleSpecification()))
			return false;
		if(!vehicleOptions.equals(other.getVehicleOptions()))
			return false;
		return true;
	}
}