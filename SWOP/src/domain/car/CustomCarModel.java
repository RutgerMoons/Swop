package domain.car;

import java.util.HashMap;
import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

public class CustomCarModel implements ICarModel {

	private HashMap<CarOptionCategory, CarOption> carOptions;
	
	public CustomCarModel(){
		carOptions = new HashMap<>();
	}
	
	@Override
	public Map<CarOptionCategory, CarOption> getCarParts() {
		return carOptions;
	}

	@Override
	public void addCarPart(CarOption part) throws AlreadyInMapException,
			ImmutableException {
		if(carOptions.containsKey(part.getType())){
			throw new AlreadyInMapException();
		}
		carOptions.put(part.getType(), part);
	}

	@Override
	public Map<CarOptionCategory, Boolean> getForcedOptionalTypes() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void addForcedOptionalType(CarOptionCategory type, boolean bool)
			throws ImmutableException, NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public CarModelSpecification getSpecification() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void setSpecification(CarModelSpecification template)
			throws ImmutableException, NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String toString(){
		String line = System.lineSeparator();
		String result = "Custom order: ";
		for(CarOptionCategory type: carOptions.keySet()){
			CarOption option = carOptions.get(type);
			result+= line + type.toString() + ": " +option.getDescription();
		}
		return result;
	}
}
