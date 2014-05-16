package domain.vehicle;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import domain.assembly.workBench.WorkBenchType;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing a template from which a Vehicle can be built. 
 * It contains of a map of VehicleOptions, belonging to its VehicleOptionCategory. 
 * It also knows how long it has to be on a WorkBench with its WorkbenchType.
 */
public class VehicleSpecification {

	private String description; 
	private Map<WorkBenchType, Integer> timeAtWorkBench;
	private Multimap<VehicleOptionCategory, VehicleOption> parts;

	/**
	 * Create a new VehicleSpecification.
	 * 
	 * @param 	description
	 * 			The description of the VehicleSpecification, often a model name
	 * 
	 * @param 	parts
	 * 			The VehicleOptions that can be used to build a vehicle
	 * 
	 * @param 	timeAtWorkBench
	 * 			The time it has to spend on certain WorkBenches
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the parameters is null or if the description is empty
	 */
	public VehicleSpecification(String description, Set<VehicleOption> parts,
			Map<WorkBenchType, Integer> timeAtWorkBench) {
		if(description==null || description.isEmpty() || parts==null || timeAtWorkBench==null){
			throw new IllegalArgumentException();
		}
		this.setDescription(description);
		this.setTimeAtWorkBench(timeAtWorkBench);
		this.parts = HashMultimap.create();
		addAll(parts);
	}

	private void addAll(Set<VehicleOption> parts) {
		for (VehicleOption part : parts) {
			this.parts.put(part.getType(), part);
		}
	}

	/**
	 * Get the name of this VehicleSpecification.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the name of the VehicleSpecification.
	 * 
	 * @param	description
	 * 			The name to give the VehicleSpecification
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the description is null or is empty
	 */
	public void setDescription(String description) {
		if(description==null || description.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.description = description;
	}
	
	/**
	 * Get the time the VehicleSpecification has to spend on each WorkBench with its WorkbenchType.
	 */
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(timeAtWorkBench);
	}

	private void setTimeAtWorkBench(Map<WorkBenchType, Integer> timeAtWorkBench) {
		this.timeAtWorkBench = timeAtWorkBench;
	}

	/**
	 * Get the VehicleOptions that this VehicleSpecification has to offer. 
	 */
	public Multimap<VehicleOptionCategory, VehicleOption> getVehicleOptions() {
		return new ImmutableMultimap.Builder<VehicleOptionCategory, VehicleOption>().putAll(parts).build();
	}

	@Override
	public String toString(){
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((parts == null) ? 0 : parts.hashCode());
		result = prime * result
				+ ((timeAtWorkBench == null) ? 0 : timeAtWorkBench.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VehicleSpecification other = (VehicleSpecification) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (parts == null) {
			if (other.parts != null)
				return false;
		} else if (!parts.equals(other.parts))
			return false;
		if (timeAtWorkBench == null) {
			if (other.timeAtWorkBench != null)
				return false;
		} else if (!timeAtWorkBench.equals(other.timeAtWorkBench))
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param	workBenchType used to determine the time this job will take to complete
	 * 
	 * @return	The amount of minutes to complete this VehicleSpecification at the given type of WorkBench
	 */
	public int getProductionTime(WorkBenchType workBenchType) {
		return getTimeAtWorkBench().get(workBenchType);
	}

}
