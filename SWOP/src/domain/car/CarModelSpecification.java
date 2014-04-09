package domain.car;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CarModelSpecification {

	private String description;
	private int timeAtWorkBench;
	private Multimap<CarOptionCategory, CarOption> parts;

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
