package domain.vehicle.catalogue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import domain.vehicle.vehicle.CustomVehicle;

/**
 * A class representing a catalogue that consists of CustomVehicles. 
 *
 */
public class CustomVehicleCatalogue {

		private Multimap<String, CustomVehicle> data;

		/**
		 * Create a new CustomVehicleCatalogue.
		 */
		public CustomVehicleCatalogue() {
			data = HashMultimap.create();
		}

		/**
		 * Get the catalogue with the CustomVehicles in.
		 */
		public Multimap<String, CustomVehicle> getCatalogue() {
			return new ImmutableMultimap.Builder<String, CustomVehicle>().putAll(data).build();
		}


		/**
		 * Add a new model to the catalogue.
		 * 
		 * @param 	model
		 * 			The model you want to add
		 * 
		 * @throws 	IllegalArgumentException
		 *         	Thrown when the description is null or empty or the model is null
		 */
		public void addModel(String description, CustomVehicle model) {
			if (description==null || description.isEmpty() || model == null)
				throw new IllegalArgumentException();
			data.put(description, model);
		}
}
