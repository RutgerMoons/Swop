package domain.vehicle;

import java.util.HashMap;
import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;

/**
 * Represents a set of CarOptions used in the creation of Single Task Orders by Custom Car Shop Managers.
 */
public class CustomVehicle implements IVehicle {

	private HashMap<VehicleOptionCategory, IVehicleOption> vehicleOptions;
	
	public CustomVehicle(){
		vehicleOptions = new HashMap<>();
	}
	
	@Override
	public Map<VehicleOptionCategory, IVehicleOption> getVehicleOptions() {
		return vehicleOptions;
	}

	@Override
	public void addCarPart(IVehicleOption part) throws AlreadyInMapException,
			UnmodifiableException {
		if(part==null){
			throw new IllegalArgumentException();
		}
		if(vehicleOptions.containsKey(part.getType())){
			throw new AlreadyInMapException();
		}
		vehicleOptions.put(part.getType(), part);
	}

	@Override
	public Map<IVehicleOption, Boolean> getForcedOptionalTypes() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void addForcedOptionalType(IVehicleOption type, boolean bool)
			throws UnmodifiableException, NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public VehicleSpecification getSpecification() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void setSpecification(VehicleSpecification template)
			throws UnmodifiableException, NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String toString(){
		String line = System.lineSeparator();
		String result = "";
		for(VehicleOptionCategory type: vehicleOptions.keySet()){
			IVehicleOption option = vehicleOptions.get(type);
			result+= line + type.toString() + ": " +option.getDescription();
		}
		result = result.replaceFirst(line, "");
		return result;
	}

	@Override
	public int getTimeAtWorkBench() {
		return 60;
	}
}

