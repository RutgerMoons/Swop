package car;

import java.util.HashMap;
/**
 * Class representing a certain carmodel. Each carmodel has all the essential carparts.
 *
 */
public class CarModel {

	private String description;
	private HashMap<Class<?>,CarPart> carParts;

	public CarModel(String description, Airco airco, Body body, Color color, Engine engine, Gearbox gear, Seat seat, Wheel wheel){
		carParts = new HashMap<Class<?>,CarPart>();
		this.setDescription(description);
		this.setCarParts(airco,body,color,engine,gear,seat,wheel);
	}

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


	public HashMap<Class<?>,CarPart> getCarParts(){
		return this.carParts;
	}


	private void setDescription(String description){
		if(!description.equals(null) && !description.equals(""))
			this.description=description;
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

	public String getDescription(){
		return this.description;
	}
	
	public String toString(){
		return getDescription();
	}
}
