package domain.restriction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import domain.exception.AlreadyInMapException;
import domain.vehicle.IVehicleOption;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

/**
 * A PartPicker has all the tools to compose a CarModel.
 */
public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private Vehicle model;

	/**
	 * Creates a new PartPicker.
	 * @param bindingRestrictions
	 * 			The BindingRestrictions which this PartPicker takes into account.
	 * @param optionalRestrictions
	 * 			The OptionalRestrictions which this PartPicker takes into account.
	 */
	public PartPicker(Set<BindingRestriction> bindingRestrictions,
			Set<OptionalRestriction> optionalRestrictions) {
		setBindingRestrictions(bindingRestrictions);
		setOptionalRestrictions(optionalRestrictions);
	}

	/**
	 * Creates a new CarModel that will be built according to the CarModelSpecification, by this PartPicker.
	 * @param template
	 * 			The CarModelSpecification according to which this CarModel will be built.
	 */
	public void setNewModel(VehicleSpecification template) {
		for (OptionalRestriction restriction : optionalRestrictions) {
			restriction.setRestrictedPartAlreadyChosen(false);
		}
		model = new Vehicle(template);
	}

	/**
	 * Adds a CarOption to the CarModel this PartPicker is currently building.
	 * @param part
	 * 			The CarOption to be added to the model.
	 * @throws AlreadyInMapException
	 * 			If the CarModel already contains a CarOption of the same type.
	 */
	public void addCarPartToModel(VehicleOption part) throws AlreadyInMapException {
		model.addCarPart(part);
	}

	/**
	 * Get the CarOptions of the given CarOptionCategory that can still be chosen for the model.
	 * @param type
	 * 			The CarOptionCategory of which the still available CarOptions will be returned.
	 * @return
	 * 		A Collection that contains the still available CarOptions of the given CarOptionCategory.
	 */
	public List<IVehicleOption> getStillAvailableCarParts(
			VehicleOptionCategory type) {
		Collection<VehicleOption> availableParts = new HashSet<>();

		availableParts = checkBindingRestrictions(type);
		availableParts = removeConflictingBindingParts(type, availableParts);
		availableParts = checkOptionalRestrictions(type, availableParts);

		List<IVehicleOption> options = new ArrayList<>();
		options.addAll(availableParts);
		return options;
	}

	/**
	 * Makes sure that the optionality of the correct CarOptionCategories is overridden for the current CarModel, according to the OptionalRestrictions.
	 * Also makes sure that if a restriction can't be fulfilled anymore due to already chosen parts, the CarOption in the first part of the restriction can't be chosen anymore. 
	 */
	private Collection<VehicleOption> checkOptionalRestrictions(
			VehicleOptionCategory type, Collection<VehicleOption> availableParts) {

		for (OptionalRestriction restriction : optionalRestrictions) {
			Map<VehicleOptionCategory, VehicleOption> parts = model.getCarParts();
			if (restriction.getCarPart().getType().equals(type)) {				
				if (!restriction.getRestrictedPartAlreadyChosen()) {
					model.addForcedOptionalType(restriction.getCarPart(),
							restriction.isOptional());
				} else if (restriction.getRestrictedPartAlreadyChosen()			
						&& !parts.containsKey(restriction.getCarPartType())) {  
					availableParts.remove(restriction.getCarPart());			
				}

			} else if (restriction.getCarPartType().equals(type)) {
				if (restriction.getCarPartType().equals(type)
						&& !restriction.isOptional()) {
					restriction.setRestrictedPartAlreadyChosen(true);
				}
			}

		}
		return availableParts;
	}

	/**
	 * Returns the CarOptions which can still be chosen after the current CarModel has been checked with all the BindingRestrictions. 
	 * @param type
	 * 		The CarOptionCategory of which the still available CarOptions will be returned.
	 * @return
	 * 		The still available CarOptions as a Collection.
	 */
	private Collection<VehicleOption> checkBindingRestrictions(
			VehicleOptionCategory type) {
		Set<VehicleOption> availableParts = new HashSet<>();

		for (BindingRestriction restriction : bindingRestrictions) {
			if (model.getCarParts().values()
					.contains(restriction.getChosenCarPart())
					&& restriction.getRestrictedCarPart().getType().equals(type)
					&& model.getSpecification().getCarParts().values()
					.contains(restriction.getRestrictedCarPart())) {
				availableParts.add(restriction.getRestrictedCarPart());
			}
		}

		if (availableParts.isEmpty()){
			for(VehicleOption option: model.getSpecification().getCarParts().get(type)){
				availableParts.add(option);
			}
		}
		return availableParts;
	}

	/**
	 * Removes CarOptions from the Collection of still available CarOptions 
	 * if, due to the already chosen parts in the model,
	 * a BindingRestriction can't be fulfilled anymore. 
	 * (If the second part of the  restriction can't be fulfilled anymore, the CarOption in the first part is removed from the Collection.)
	 * @param type
	 * 		The CarOptionCategory of which the still available parts will be returned.
	 * @param availableParts
	 * 		The CarOptions that were previously selected to still be available.
	 * @return
	 * 		The CarOptions that are still available after the right CarOptions are removed from the given Collection.
	 */
	private Collection<VehicleOption> removeConflictingBindingParts(
			VehicleOptionCategory type, Collection<VehicleOption> availableParts) {

		Multimap<VehicleOption, VehicleOption> options = HashMultimap.create();
		for (BindingRestriction restriction : bindingRestrictions) {
			if (restriction.getChosenCarPart().getType().equals(type)) {
				options.put(restriction.getChosenCarPart(),
						restriction.getRestrictedCarPart());
			}
		}

		for (VehicleOption option1 : options.keySet()) {
			for (VehicleOption option2 : options.get(option1)) {
				VehicleOption inModel = model.getCarParts().get(option2.getType());
				if (inModel != null && !options.get(option1).contains(inModel)) {
					availableParts.remove(option1);
				}

			}
		}

		return availableParts;
	}

	/**
	 * Get the CarModel that has been built or is being build by the PartPicker.
	 */
	public Vehicle getModel() {
		return model;
	}

	/**
	 * Add a BindingRestriction that the PartPicker has to take into account.
	 */
	public void addBindingRestriction(BindingRestriction restriction) {
		bindingRestrictions.add(restriction);

	}

	/**
	 * Add an OptionalRestriction that the PartPicker has to take into account.
	 */
	public void addOptionalRestriction(OptionalRestriction restriction) {
		optionalRestrictions.add(restriction);
	}

	/**
	 * Get the BindingRestrictions the PartPicker has to take into account.
	 */
	public Set<BindingRestriction> getBindingRestrictions() {
		return Collections.unmodifiableSet(bindingRestrictions);
	}

	/**
	 * Set the BindingRestrictions the PartPicker has to take into account.
	 */
	public void setBindingRestrictions(
			Set<BindingRestriction> bindingRestrictions) {
		this.bindingRestrictions = bindingRestrictions;
	}

	/**
	 * Get the OptionalRestrictions the PartPicker has to take into account.
	 */
	public Set<OptionalRestriction> getOptionalRestrictions() {
		return Collections.unmodifiableSet(optionalRestrictions);
	}

	/**
	 * Set the OptionalRestrictions the PartPicker has to take into account.
	 */
	public void setOptionalRestrictions(
			Set<OptionalRestriction> optionalRestrictions) {
		this.optionalRestrictions = optionalRestrictions;
	}

	public VehicleSpecification getSpecification(String specificationName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getVehicleSpecifications() {
		// TODO Auto-generated method stub
		return null;
	}

}
