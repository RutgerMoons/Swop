package domain.assembly.workBench;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing the type of a WorkBench.
 */
public enum WorkBenchType {

	ACCESSORIES(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.SEATS, VehicleOptionCategory.AIRCO, VehicleOptionCategory.WHEEL, VehicleOptionCategory.SPOILER))), 
	BODY(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.BODY, VehicleOptionCategory.COLOR))), 
	CARGO(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.CARGO))), 
	CERTIFICATION(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.CERTIFICATION))), 
	DRIVETRAIN(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.ENGINE, VehicleOptionCategory.GEARBOX)));
	
	private Set<VehicleOptionCategory> responsibilities;

	private WorkBenchType(Set<VehicleOptionCategory> responsibilities) {
		this.responsibilities = responsibilities;
	}
	
	/**
	 * Get the responsibilities for the WorkBenchType.
	 */
	public Set<VehicleOptionCategory> getResponsibilities(){
		return Collections.unmodifiableSet(responsibilities);
	}
}
