package domain.vehicle;

import domain.exception.UnmodifiableException;

public class UnmodifiableVehicleOption implements IVehicleOption{

	private VehicleOption option;

	public UnmodifiableVehicleOption(VehicleOption option){
		this.option = option;
	}
	@Override
	public void setDescription(String description) throws UnmodifiableException{
		throw new UnmodifiableException();
	}

	@Override
	public String getDescription() {
		return option.getDescription();
	}

	@Override
	public VehicleOptionCategory getType() {
		return option.getType();
	}

	@Override
	public void setType(VehicleOptionCategory type) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public String getTaskDescription() {
		return option.getTaskDescription();
	}

	@Override
	public String getActionDescription() {
		// TODO Auto-generated method stub
		return option.getActionDescription();
	}

	
	
}
