package domain.car;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Class representing a CarModelSpecification.
 * Objects of this class represent a template according to which CarModels can be put together.
 *
 */
public class CarModelSpecification {

	private String description;
	private int timeAtWorkBench;
	private Multimap<CarOptionCategory, CarOption> parts;

	/**
	 * Creates a new CarModelSpecification, which consists of a number of CarOptions 
	 * 	and contains the description (name) of this model and the time a CarModel of this type usually spends at a workbench.
	 */
	public CarModelSpecification(String description, Set<CarOption> parts,
			int timeAtWorkBench) {
		this.setDescription(description);
		this.setTimeAtWorkBench(timeAtWorkBench);
		this.parts = HashMultimap.create();
		addAll(parts);
	}

	private void addAll(Set<CarOption> parts) {
		for (CarOption part : parts) {
			this.parts.put(part.getType(), part);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTimeAtWorkBench() {
		return timeAtWorkBench;
	}

	public void setTimeAtWorkBench(int timeAtWorkBench) {
		this.timeAtWorkBench = timeAtWorkBench;
	}

	public Multimap<CarOptionCategory, CarOption> getCarParts() {
		return parts;
	}

	@Override
	public String toString(){
		return description;
	}

}
