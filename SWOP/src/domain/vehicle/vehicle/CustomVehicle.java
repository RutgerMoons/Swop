package domain.vehicle.vehicle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import domain.assembly.workBench.WorkbenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * TODO doc
 * Represents a set of VehicleOptions used in the creation of Single Task Orders by Custom Car Shop Managers.
 */
public class CustomVehicle implements IVehicle {

	private HashMap<VehicleOptionCategory, VehicleOption> vehicleOptions;
	private Map<WorkbenchType, Integer> timeAtWorkbench;
	public CustomVehicle(){
		vehicleOptions = new HashMap<>();
		timeAtWorkbench = new HashMap<>();
		
		for(WorkbenchType type: WorkbenchType.values()){
			timeAtWorkbench.put(type, 60);
		}
	}
	
	@Override
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableMap(vehicleOptions);
	}

	@Override
	public void addCarPart(VehicleOption part) throws AlreadyInMapException {
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
	public void addForcedOptionalType(VehicleOption type, boolean bool)
			throws UnmodifiableException, NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public VehicleSpecification getSpecification() {
		throw new NotImplementedException();
	}

	@Override
	public void setSpecification(VehicleSpecification template) {
		throw new NotImplementedException();
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
	public Map<WorkbenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(timeAtWorkbench);
	}

	@Override
	public boolean canBeHandled(Set<VehicleSpecification> responsibilities) {
		return true;
	}
}

