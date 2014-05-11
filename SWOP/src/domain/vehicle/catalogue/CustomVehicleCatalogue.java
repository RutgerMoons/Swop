package domain.vehicle.catalogue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import domain.vehicle.vehicle.CustomVehicle;

/**
 * This class represents a catalogue that consists of CustomCarModels. 
 *
 */
public class CustomVehicleCatalogue {

	// private HashMap<String, HashMap<Class<?>, CarPart>> data;
		private Multimap<String, CustomVehicle> data;

		/**
		 * Default constructor. When a carModelCatalogue is constructed, a
		 * carModelCatalogueFiller is created and the newly constructed
		 * carModelCatalogue is filled with the basic carModels thanks to the
		 * carModelCatalogueFiller.
		 */
		public CustomVehicleCatalogue() {
			data = HashMultimap.create();
		}

		/**
		 * Returns an immutable Map of carmodels with their names.
		 */
		public Multimap<String, CustomVehicle> getCatalogue() {
			return data;
		}


		/**
		 * Add a new model to the catalogue.
		 * 
		 * @param model
		 *            The model you want to add.
		 * @throws IllegalArgumentException
		 *             if model==null or not is valid
		 */
		public void addModel(String description, CustomVehicle model) {
			if (description==null || description.isEmpty() || model == null)
				throw new IllegalArgumentException();
			data.put(description, model);
		}
}
