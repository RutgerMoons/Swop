package domain.car;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;

/**
 * This class is used to fill the CustomCarModelCatalogue.  
 *
 */
public class CustomCarModelCatalogueFiller {

	/**
	 * Get the initial CustomCarModels that can be added to the catalogue. 
	 */
	public Multimap<String, CustomCarModel> getInitialModels(){
		Multimap<String, CustomCarModel> models = HashMultimap.create();
		models.putAll(getSeats());
		models.putAll(getColors());
		return new ImmutableMultimap.Builder<String, CustomCarModel>().putAll(models).build();
	}

	private Multimap<String, CustomCarModel> getSeats() {
		Multimap<String, CustomCarModel> models = HashMultimap.create();
		CustomCarModel customSeats = new CustomCarModel();
		try {
			customSeats.addCarPart(new CarOption("custom", CarOptionCategory.SEATS));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		models.put("installing custom seats", customSeats);
		return models;
	}

	private Multimap<String, CustomCarModel> getColors() {
		Multimap<String, CustomCarModel> models = HashMultimap.create();
		
		CustomCarModel black = new CustomCarModel();
		try {
			black.addCarPart(new CarOption("black", CarOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomCarModel red = new CustomCarModel();
		try {
			red.addCarPart(new CarOption("red", CarOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomCarModel blue = new CustomCarModel();
		try {
			blue.addCarPart(new CarOption("blue", CarOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomCarModel white = new CustomCarModel();
		try {
			white.addCarPart(new CarOption("white", CarOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomCarModel green = new CustomCarModel();
		try {
			green.addCarPart(new CarOption("green", CarOptionCategory.COLOR));
		} catch (AlreadyInMapException | ImmutableException e) {
		}
		
		CustomCarModel yellow = new CustomCarModel();
		try {
			yellow.addCarPart(new CarOption("yellow", CarOptionCategory.COLOR));
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
