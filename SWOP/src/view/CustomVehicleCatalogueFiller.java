package view;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * This class is used to fill the CustomVehicleCatalogue.  
 */
public class CustomVehicleCatalogueFiller {

	/**
	 * Get the initial Vehicles that can be added to the catalogue. 
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
		customSeats.addVehicleOption(new VehicleOption("custom", VehicleOptionCategory.SEATS));
		models.put("installing custom seats", customSeats);
		return models;
	}

	private Multimap<String, CustomVehicle> getColors() {
		Multimap<String, CustomVehicle> models = HashMultimap.create();

		CustomVehicle black = new CustomVehicle();
		black.addVehicleOption(new VehicleOption("black", VehicleOptionCategory.COLOR));

		CustomVehicle red = new CustomVehicle();
		red.addVehicleOption(new VehicleOption("red", VehicleOptionCategory.COLOR));

		CustomVehicle blue = new CustomVehicle();
		blue.addVehicleOption(new VehicleOption("blue", VehicleOptionCategory.COLOR));

		CustomVehicle white = new CustomVehicle();
		white.addVehicleOption(new VehicleOption("white", VehicleOptionCategory.COLOR));

		CustomVehicle green = new CustomVehicle();
		green.addVehicleOption(new VehicleOption("green", VehicleOptionCategory.COLOR));

		CustomVehicle yellow = new CustomVehicle();
		yellow.addVehicleOption(new VehicleOption("yellow", VehicleOptionCategory.COLOR));

		models.put("spraying car bodies",yellow);
		models.put("spraying car bodies",green);
		models.put("spraying car bodies",white);
		models.put("spraying car bodies",blue);
		models.put("spraying car bodies",red);
		models.put("spraying car bodies",black);

		return models;
	}

}