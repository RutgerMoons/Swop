package car;

import java.util.ArrayList;

public class CarModel {

	private String description;
	private ArrayList<CarPart> carParts;
	
	public CarModel(String description, Airco airco, Body body, Color color, Engine engine, Gearbox gear, Seat seat, Wheel wheel){
		carParts = new ArrayList<CarPart>();
		this.setDescription(description);
		this.setCarParts(airco,body,color,engine,gear,seat,wheel);
	}

	private void setCarParts(Airco airco, Body body, Color color,
			Engine engine, Gearbox gear, Seat seat, Wheel wheel) {
		if(airco!= null && body!=null && color != null && engine != null && gear != null && seat != null && wheel != null){
			this.carParts.add(wheel);
			this.carParts.add(seat);
			this.carParts.add(gear);
			this.carParts.add(engine);
			this.carParts.add(color);
			this.carParts.add(body);
			this.carParts.add(airco);
		}
		else
			throw new IllegalArgumentException();
	}


	public ArrayList<CarPart> getCarParts(){
		return this.carParts;
	}


	private void setDescription(String description){
		if(!description.equals(null) && !description.equals(""))
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}
}
