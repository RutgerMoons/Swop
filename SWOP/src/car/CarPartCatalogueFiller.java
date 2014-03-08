package car;

import java.util.ArrayList;

public class CarPartCatalogueFiller {
	
	private CarPartCatalogue catalogue;
	
	public CarPartCatalogueFiller(CarPartCatalogue cat){
		this.catalogue = cat;
	}

	private ArrayList<CarPart> getInitialAirco() {
		ArrayList<CarPart> initialAircos = new ArrayList<CarPart>();
		initialAircos.add(new Airco("manual"));
		initialAircos.add(new Airco("automatic climate control"));
		return initialAircos;
	}
	
	private ArrayList<CarPart> getInitialBody() {
		ArrayList<CarPart> initialBodies = new ArrayList<CarPart>();
		initialBodies.add(new Body("break"));
		initialBodies.add(new Body("sedan"));
		return initialBodies;
	}
	
	private ArrayList<CarPart> getInitialColors() {
		ArrayList<CarPart> initialColors = new ArrayList<CarPart>();
		initialColors.add(new Color("red"));
		initialColors.add(new Color("blue"));
		initialColors.add(new Color("black"));
		initialColors.add(new Color("white"));
		return initialColors;
	}
	
	private ArrayList<CarPart> getInitialEngine() {
		ArrayList<CarPart> initialEngines = new ArrayList<CarPart>();
		initialEngines.add(new Engine("standard 2l 4 cilinders"));
		initialEngines.add(new Engine("performance 2.5l 6 cilinders"));
		return initialEngines;
	}
	
	private ArrayList<CarPart> getInitialGearbox() {
		ArrayList<CarPart> initialGearBox = new ArrayList<CarPart>();
		initialGearBox.add(new Gearbox("6 speed manual"));
		initialGearBox.add(new Gearbox("5 speed automatic"));
		return initialGearBox;
	}
	
	private ArrayList<CarPart> getInitialSeat() {
		ArrayList<CarPart> initialSeats = new ArrayList<CarPart>();
		initialSeats.add(new Seat("leather black"));
		initialSeats.add(new Seat("leather white"));
		initialSeats.add(new Seat("vinyl grey"));
		return initialSeats;
	}
	
	private ArrayList<CarPart> getInitialWheel() {
		ArrayList<CarPart> initialWheels = new ArrayList<CarPart>();
		initialWheels.add(new Wheel("comfort"));
		initialWheels.add(new Seat("sports (low profile)"));
		return initialWheels;
	}
	
	public void initializeCarParts(){
		this.catalogue.addListForCarPart(Airco.class, this.getInitialAirco());
		this.catalogue.addListForCarPart(Body.class, this.getInitialBody());
		this.catalogue.addListForCarPart(Color.class, this.getInitialColors());
		this.catalogue.addListForCarPart(Engine.class, this.getInitialEngine());
		this.catalogue.addListForCarPart(Gearbox.class, this.getInitialGearbox());
		this.catalogue.addListForCarPart(Seat.class, this.getInitialSeat());
		this.catalogue.addListForCarPart(Wheel.class, this.getInitialWheel());
	}
}
