package domain.car;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Class representing a CarModelSpecification.
 * Objects of this class represent a template according to which CarModels can be put together.
 *
 */
public class VehicleSpecification {

	private String description;
	private int timeAtWorkBench;
	private Multimap<VehicleOptionCategory, VehicleOption> parts;

	/**
	 * Creates a new CarModelSpecification, which consists of a number of CarOptions 
	 * 	and contains the description (name) of this model and the time a CarModel of this type usually spends at a workbench.
	 */
	public VehicleSpecification(String description, Set<VehicleOption> parts,
			int timeAtWorkBench) {
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
	public int getTimeAtWorkBench() {
		return timeAtWorkBench;
	}

	/**
	 * Set the time the specification has to spend on a workbench.
	 */
	public void setTimeAtWorkBench(int timeAtWorkBench) {
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

}
