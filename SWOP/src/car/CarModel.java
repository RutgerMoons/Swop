package car;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
/**
 * Class representing a certain carmodel. Each carmodel has all the essential carparts.
 *
 */
public class CarModel implements ICarModel{

	private String description;
	private HashMap<Class<?>,CarPart> carParts;

	public CarModel(String description, Airco airco, Body body, Color color, Engine engine, Gearbox gear, Seat seat, Wheel wheel){
		carParts = new HashMap<Class<?>,CarPart>();
		this.setDescription(description);
		this.setCarParts(airco,body,color,engine,gear,seat,wheel);
	}

	/**
	 * Method for assigning given objects to certain attributes.
	 * The method checks before assigning if the given objects are different from null.
	 * If this isn't the case, an IllegalArgumentException is thrown. 
	 */
	private void setCarParts(Airco airco, Body body, Color color,
			Engine engine, Gearbox gear, Seat seat, Wheel wheel) {
		if(airco!= null && body!=null && color != null && engine != null && gear != null && seat != null && wheel != null){
			this.carParts.put(Airco.class, airco);
			this.carParts.put(Body.class, body);
			this.carParts.put(Color.class, color);
			this.carParts.put(Engine.class, engine);
			this.carParts.put(Gearbox.class, gear);
			this.carParts.put(Seat.class, seat);
			this.carParts.put(Wheel.class, wheel);
		}
		else
			throw new IllegalArgumentException();
	}

	/**
	 *This method returns a deep copy of the the hashmap including all the car parts. 
	 */
	public HashMap<Class<?>,CarPart> getCarParts(){
		return copy(carParts);
	}

	/**
	 * Method that returns a deep clone of the given hashmap.
	 */
	private HashMap<Class<?>, CarPart> copy(HashMap<Class<?>, CarPart> carParts2) {
		HashMap<Class<?>, CarPart> newMap = new HashMap<Class<?>, CarPart>();
		Set<Entry<Class<?>, CarPart>> set1 = carParts2.entrySet();
		for (Entry<Class<?>, CarPart> entry : set1){
			newMap.put(entry.getKey(), entry.getValue());
		}
		return newMap;
	}

	/**
	 * Method for giving naming this model. The method checks if
	 * the name is different from null and if it is different from the empty string.
	 */
	private void setDescription(String description){
		if(!description.equals(null) && !description.equals(""))
			this.description=description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carParts == null) ? 0 : carParts.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
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
		CarModel other = (CarModel) obj;
		if (!carParts.values().containsAll(other.carParts.values()))
			return false;
		if (!description.equals(other.description))
			return false;
		return true;
	}

	/**
	 * Returns the description/name of the model. Since a String
	 * is immutable, a clone is not necessary.
	 */
	public String getDescription(){
		return this.description;
	}

	@Override
	public String toString(){
		return getDescription();
	}
}
