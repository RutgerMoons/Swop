package car;

import java.util.HashSet;
import java.util.Set;
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
	private Set<CarPart> getInitialAirco() {
		Set<CarPart> initialAircos = new HashSet<CarPart>();
		initialAircos.add(new Airco("manual"));
		initialAircos.add(new Airco("automatic climate control"));
		return initialAircos;
	}
	
	/**
	 * Initialization of the possible initial Bodies.
	 */
	private Set<CarPart> getInitialBody() {
		Set<CarPart> initialBodies = new HashSet<CarPart>();
		initialBodies.add(new Body("break"));
		initialBodies.add(new Body("sedan"));
		return initialBodies;
	}
	
	/**
	 * Initialization of the possible initial Colors.
	 */
	private Set<CarPart> getInitialColors() {
		Set<CarPart> initialColors = new HashSet<CarPart>();
		initialColors.add(new Color("red"));
		initialColors.add(new Color("blue"));
		initialColors.add(new Color("black"));
		initialColors.add(new Color("white"));
		return initialColors;
	}
	
	/**
	 * Initialization of the possible initial Engines.
	 */
	private Set<CarPart> getInitialEngine() {
		Set<CarPart> initialEngines = new HashSet<CarPart>();
		initialEngines.add(new Engine("standard 2l 4 cilinders"));
		initialEngines.add(new Engine("performance 2.5l 6 cilinders"));
		return initialEngines;
	}
	
	/**
	 * Initialization of the possible initial Gearboxes.
	 */
	private Set<CarPart> getInitialGearbox() {
		HashSet<CarPart> initialGearBox = new HashSet<CarPart>();
		initialGearBox.add(new Gearbox("6 speed manual"));
		initialGearBox.add(new Gearbox("5 speed automatic"));
		return initialGearBox;
	}
	
	/**
	 * Initialization of the possible initial Seats.
	 */
	private Set<CarPart> getInitialSeat() {
		HashSet<CarPart> initialSeats = new HashSet<CarPart>();
		initialSeats.add(new Seat("leather black"));
		initialSeats.add(new Seat("leather white"));
		initialSeats.add(new Seat("vinyl grey"));
		return initialSeats;
	}
	
	/**
	 * Initialization of the possible initial Wheels.
	 */
	private Set<CarPart> getInitialWheel() {
		HashSet<CarPart> initialWheels = new HashSet<CarPart>();
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
