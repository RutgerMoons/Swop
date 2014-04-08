package domain.car;

import java.util.Set;

import com.google.common.collect.Multimap;

public class CarModelTemplate {

	private String description;
	private int timeAtWorkBench;
	private Multimap<CarPartType, CarPart> parts;

	public CarModelTemplate(String description, Set<CarPart> parts,
			int timeAtWorkBench) {
		this.setDescription(description);
		this.setTimeAtWorkBench(timeAtWorkBench);
		addAll(parts);
	}

	private void addAll(Set<CarPart> parts) {
		for (CarPart part : parts) {
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

	public Multimap<CarPartType, CarPart> getCarParts() {
		return parts;
	}


}
