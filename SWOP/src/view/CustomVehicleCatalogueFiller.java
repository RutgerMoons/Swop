package view;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import domain.car.CustomVehicle;
import domain.car.VehicleOption;
import domain.car.VehicleOptionCategory;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;

/**
 * This class is used to fill the CustomCarModelCatalogue.  
 *
 */
public class CustomVehicleCatalogueFiller {

	/**
	 * Get the initial CustomCarModels that can be added to the catalogue. 
	 */
	public Multimap<String, CustomVehicle> getInitialModels(){
		Multimap<String, CustomVehicle> models = HashMultimap.create();
		models.putAll(getSeats());
		models.putAll(getColors());
		return new ImmutableMultimap.Builder<String, CustomVehicle>().putAll(models).build();
	}

	private Multimap<String, CustomVehicle> getSeats() {
		Multimap<String, CustomVehicle> models = HashMultimap.create();
		CustomVehicle customSeats = new CustomVehicle();
		try {
			customSeats.addCarPart(new VehicleOption("custom", VehicleOptionCategory.SEATS));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		models.put("installing custom seats", customSeats);
		return models;
	}

	private Multimap<String, CustomVehicle> getColors() {
		Multimap<String, CustomVehicle> models = HashMultimap.create();
		
		CustomVehicle black = new CustomVehicle();
		try {
			black.addCarPart(new VehicleOption("black", VehicleOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomVehicle red = new CustomVehicle();
		try {
			red.addCarPart(new VehicleOption("red", VehicleOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomVehicle blue = new CustomVehicle();
		try {
			blue.addCarPart(new VehicleOption("blue", VehicleOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomVehicle white = new CustomVehicle();
		try {
			white.addCarPart(new VehicleOption("white", VehicleOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomVehicle green = new CustomVehicle();
		try {
			green.addCarPart(new VehicleOption("green", VehicleOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomVehicle yellow = new CustomVehicle();
		try {
			yellow.addCarPart(new VehicleOption("yellow", VehicleOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		models.put("spraying car bodies",yellow);
		models.put("spraying car bodies",green);
		models.put("spraying car bodies",white);
		models.put("spraying car bodies",blue);
		models.put("spraying car bodies",red);
		models.put("spraying car bodies",black);
		
		return models;
	}
	
}
