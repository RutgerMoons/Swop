package code.car;

import java.util.ArrayList;
/**
 * Class for initial filling of the carPartCatalogue.
 * This method is associated with a given carPartCatalogue. That way
 * the class knows which carPartCatalogue to initially fill. *
 */
public class CarPartCatalogueFiller {
	
	private CarPartCatalogue catalogue;
	
	/**
	 * The given carPartCatalogue is associated with the newly constructed
	 * carPartCatalogueFiller. That way the class knows which carPartCatalogue to fill.
	 */
	public CarPartCatalogueFiller(CarPartCatalogue cat){
		this.catalogue = cat;
	}

	/**
	 * Initialization of the possible initial Airco's.
	 */
	private ArrayList<CarPart> getInitialAirco() {
		ArrayList<CarPart> initialAircos = new ArrayList<CarPart>();
		initialAircos.add(new Airco("manual"));
		initialAircos.add(new Airco("automatic climate control"));
		return initialAircos;
	}
	
	/**
	 * Initialization of the possible initial Bodies.
	 */
	private ArrayList<CarPart> getInitialBody() {
		ArrayList<CarPart> initialBodies = new ArrayList<CarPart>();
		initialBodies.add(new Body("break"));
		initialBodies.add(new Body("sedan"));
		return initialBodies;
	}
	
	/**
	 * Initialization of the possible initial Colors.
	 */
	private ArrayList<CarPart> getInitialColors() {
		ArrayList<CarPart> initialColors = new ArrayList<CarPart>();
		initialColors.add(new Color("red"));
		initialColors.add(new Color("blue"));
		initialColors.add(new Color("black"));
		initialColors.add(new Color("white"));
		return initialColors;
	}
	
	/**
	 * Initialization of the possible initial Engines.
	 */
	private ArrayList<CarPart> getInitialEngine() {
		ArrayList<CarPart> initialEngines = new ArrayList<CarPart>();
		initialEngines.add(new Engine("standard 2l 4 cilinders"));
		initialEngines.add(new Engine("performance 2.5l 6 cilinders"));
		return initialEngines;
	}
	
	/**
	 * Initialization of the possible initial Gearboxes.
	 */
	private ArrayList<CarPart> getInitialGearbox() {
		ArrayList<CarPart> initialGearBox = new ArrayList<CarPart>();
		initialGearBox.add(new Gearbox("6 speed manual"));
		initialGearBox.add(new Gearbox("5 speed automatic"));
		return initialGearBox;
	}
	
	/**
	 * Initialization of the possible initial Seats.
	 */
	private ArrayList<CarPart> getInitialSeat() {
		ArrayList<CarPart> initialSeats = new ArrayList<CarPart>();
		initialSeats.add(new Seat("leather black"));
		initialSeats.add(new Seat("leather white"));
		initialSeats.add(new Seat("vinyl grey"));
		return initialSeats;
	}
	
	/**
	 * Initialization of the possible initial Wheels.
	 */
	private ArrayList<CarPart> getInitialWheel() {
		ArrayList<CarPart> initialWheels = new ArrayList<CarPart>();
		initialWheels.add(new Wheel("comfort"));
		initialWheels.add(new Wheel("sports (low profile)"));
		return initialWheels;
	}
	
	/**
	 * Method for initializing all the different carParts.
	 */
	public void initializeCarParts(){
		for(CarPart ac: this.getInitialAirco())
			this.catalogue.addCarPart(Airco.class, ac);
		for(CarPart ac: this.getInitialBody())
			this.catalogue.addCarPart(Body.class, ac);
		for(CarPart ac: this.getInitialColors())
			this.catalogue.addCarPart(Color.class, ac);
		for(CarPart ac: this.getInitialEngine())
			this.catalogue.addCarPart(Engine.class, ac);
		for(CarPart ac: this.getInitialGearbox())
			this.catalogue.addCarPart(Gearbox.class, ac);
		for(CarPart ac: this.getInitialSeat())
			this.catalogue.addCarPart(Seat.class, ac);
		for(CarPart ac: this.getInitialWheel())
			this.catalogue.addCarPart(Wheel.class, ac);
		
	}
}
