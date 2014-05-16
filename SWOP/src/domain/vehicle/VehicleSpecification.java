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
 * Class representing a CarModelSpecification.
 * Objects of this class represent a template according to which CarModels can be put together.
 *
 */
public class VehicleSpecification {

	private String description; 
	private Map<WorkBenchType, Integer> timeAtWorkBench;
	private Multimap<VehicleOptionCategory, VehicleOption> parts;

	/**
	 * Creates a new CarModelSpecification, which consists of a number of CarOptions 
	 * 	and contains the description (name) of this model and the time a CarModel of this type usually spends at a workbench.
	 */
	public VehicleSpecification(String description, Set<VehicleOption> parts,
			Map<WorkBenchType, Integer> timeAtWorkBench) {
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
	 * Get the name of the specification.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the name of the specification.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the time the specification has to spend on a workbench.
	 */
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(timeAtWorkBench);
	}

	/**
	 * Set the time the specification has to spend on a workbench.
	 */
	private void setTimeAtWorkBench(Map<WorkBenchType, Integer> timeAtWorkBench) {
		this.timeAtWorkBench = timeAtWorkBench;
	}

	/**
	 * Get the CarParts that this model has to offer. 
	 */
	public Multimap<VehicleOptionCategory, VehicleOption> getCarParts() {
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
	
	public int getProductionTime(WorkBenchType workBenchType) {
		return getTimeAtWorkBench().get(workBenchType);
	}

}
