package domain.assembly.workBench;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing the type of a WorkBench. 
 * It has a set of VehicleOptionCategories representing the types of VehicleOptions 
 * that can be assembled by the WorkBenches of this type.
 */
public enum WorkBenchType {

	/**
	 * A WorkBench of this type can assemble the Seats, the Airco, the Wheels and the Spoiler.
	 */
	ACCESSORIES(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.SEATS, VehicleOptionCategory.AIRCO, VehicleOptionCategory.WHEEL, VehicleOptionCategory.SPOILER))), 
	/**
	 * A WorkBench of this type can assemble the Body and can paint a Color on the Vehicle.
	 */
	BODY(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.BODY, VehicleOptionCategory.COLOR))),
	/**
	 * A WorkBench of this type can assemble the Cargo on a truck.
	 */
	CARGO(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.CARGO))),
	/**
	 * At a WorkBench of this type, a truck can be certified.
	 */
	CERTIFICATION(new HashSet<VehicleOptionCategory>(Arrays.asList(VehicleOptionCategory.CERTIFICATION))),
	/**
	 * A WorkBench of this type can assemble the Engine and put the Gearbox in the Vehicle.
	 */
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
