package domain.car;

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
		initialAircos.add(new CarPart("manual", true, CarPartType.AIRCO));
		initialAircos.add(new CarPart("automatic climate control", true, CarPartType.AIRCO));
		return initialAircos;
	}
	
	/**
	 * Initialization of the possible initial Bodies.
	 */
	private Set<CarPart> getInitialBody() {
		Set<CarPart> initialBodies = new HashSet<CarPart>();
		initialBodies.add(new CarPart("break", false, CarPartType.BODY));
		initialBodies.add(new CarPart("sedan", false, CarPartType.BODY));
		return initialBodies;
	}
	
	/**
	 * Initialization of the possible initial Colors.
	 */
	private Set<CarPart> getInitialColors() {
		Set<CarPart> initialColors = new HashSet<CarPart>();
		initialColors.add(new CarPart("red", false, CarPartType.COLOR));
		initialColors.add(new CarPart("blue", false, CarPartType.COLOR));
		initialColors.add(new CarPart("black", false, CarPartType.COLOR));
		initialColors.add(new CarPart("white", false, CarPartType.COLOR));
		return initialColors;
	}
	
	/**
	 * Initialization of the possible initial Engines.
	 */
	private Set<CarPart> getInitialEngine() {
		Set<CarPart> initialEngines = new HashSet<CarPart>();
		initialEngines.add(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		initialEngines.add(new CarPart("performance 2.5l 6 cilinders", false, CarPartType.ENGINE));
		return initialEngines;
	}
	
	/**
	 * Initialization of the possible initial Gearboxes.
	 */
	private Set<CarPart> getInitialGearbox() {
		HashSet<CarPart> initialGearBox = new HashSet<CarPart>();
		initialGearBox.add(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		initialGearBox.add(new CarPart("5 speed automatic", false, CarPartType.GEARBOX));
		return initialGearBox;
	}
	
	/**
	 * Initialization of the possible initial Seats.
	 */
	private Set<CarPart> getInitialSeat() {
		HashSet<CarPart> initialSeats = new HashSet<CarPart>();
		initialSeats.add(new CarPart("leather black", false, CarPartType.SEATS));
		initialSeats.add(new CarPart("leather white", false, CarPartType.SEATS));
		initialSeats.add(new CarPart("vinyl grey", false, CarPartType.SEATS));
		return initialSeats;
	}
	
	/**
	 * Initialization of the possible initial Wheels.
	 */
	private Set<CarPart> getInitialWheel() {
		HashSet<CarPart> initialWheels = new HashSet<CarPart>();
		initialWheels.add(new CarPart("comfort", false, CarPartType.WHEEL));
		initialWheels.add(new CarPart("sports (low profile)", false, CarPartType.WHEEL));
		return initialWheels;
	}
	
	/**
	 * Method for initializing all the different carParts.
	 */
	public void initializeCarParts(){
		for(CarPart obj: this.getInitialAirco())
			this.catalogue.addCarPart(obj);
		for(CarPart obj: this.getInitialBody())
			this.catalogue.addCarPart(obj);
		for(CarPart obj: this.getInitialColors())
			this.catalogue.addCarPart(obj);
		for(CarPart obj: this.getInitialEngine())
			this.catalogue.addCarPart(obj);
		for(CarPart obj: this.getInitialGearbox())
			this.catalogue.addCarPart(obj);
		for(CarPart obj: this.getInitialSeat())
			this.catalogue.addCarPart(obj);
		for(CarPart obj: this.getInitialWheel())
			this.catalogue.addCarPart(obj);
		
	}
}
