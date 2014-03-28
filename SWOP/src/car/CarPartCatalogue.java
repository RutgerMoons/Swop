package car;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

/**
 * this class will be used to validate CarParts this class will be initialized
 * upon creation
 */
public class CarPartCatalogue {

	private Multimap<Class<?>, CarPart> carPartCatalogue;

	public CarPartCatalogue() {
		this.carPartCatalogue = HashMultimap.create();
	}

	/**
	 * checks whether a given CarPart is valid (three cases): CarPart is null ->
	 * false CarPart has incorrect description -> false CarPart has correct
	 * description -> true
	 * 
	 * note: the description of CarPart cannot equal null
	 */
	public boolean isValidCarPart(CarPart carPart) {
		return carPart != null && carPartCatalogue.get(carPart.getClass()).contains(carPart);
	}

	/**
	 * This methods adds an entry for a carPart if the class is valid the list
	 * is not null
	 */
	public void addCarPart(Class<?> key, CarPart value)
			throws IllegalArgumentException {
		if (key == null || value == null) {
			throw new IllegalArgumentException();
		} else {
			carPartCatalogue.put(key, value);
		}
	}

	/**
	 * Returns a deep copy clone of the carPartCatalogue.
	 */
	public Multimap<Class<?>, CarPart> getMap() {
		return new ImmutableSetMultimap.Builder<Class<?>, CarPart>()
				.putAll(this.carPartCatalogue).build();
	}

}
