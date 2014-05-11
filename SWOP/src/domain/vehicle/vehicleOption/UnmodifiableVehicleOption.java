package domain.vehicle.vehicleOption;


public class UnmodifiableVehicleOption implements IVehicleOption{

	private VehicleOption option;

	public UnmodifiableVehicleOption(VehicleOption option){
		this.option = option;
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
	public String getTaskDescription() {
		return option.getTaskDescription();
	}

	@Override
	public String getActionDescription() {
		return option.getActionDescription();
	}

	
	
}
