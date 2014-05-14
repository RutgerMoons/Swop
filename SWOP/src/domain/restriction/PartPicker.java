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
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * TODO doc aanvullen en duidelijker maken
 * A PartPicker has all the tools to compose a Vehicle
 */
public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private Vehicle model;
	private VehicleSpecificationCatalogue catalogue;

	/**
	 * Creates a new PartPicker.
	 * @param 	bindingRestrictions
	 * 				The BindingRestrictions which this PartPicker takes into account
	 * @param 	optionalRestrictions
	 * 				The OptionalRestrictions which this PartPicker takes into account
	 */
	public PartPicker(VehicleSpecificationCatalogue catalogue, Set<BindingRestriction> bindingRestrictions,
			Set<OptionalRestriction> optionalRestrictions) {
		this.setCatalogue(catalogue);
		setBindingRestrictions(bindingRestrictions);
		setOptionalRestrictions(optionalRestrictions);
	}

	/**
	 * Creates a new Vehicle that will be built according to the VehicleSpecification, by this PartPicker
	 * 
	 * @param 	template
	 * 				The VehicleSpecification according to which this CarModel will be built
	 */
	public void setNewModel(VehicleSpecification template) {
		for (OptionalRestriction restriction : optionalRestrictions) {
			restriction.setRestrictedPartAlreadyChosen(false);
		}
		model = new Vehicle(template);
	}

	/**
	 * Adds a VehicleOption to the Vehicle this PartPicker is currently building
	 * 
	 * @param 	part
	 * 				The VehicleOption to be added to the model
	 * 
	 * @throws 	AlreadyInMapException
	 * 				If the Vehicle already contains a VehicleOption of the same type
	 */
	public void addCarPartToModel(VehicleOption part) throws AlreadyInMapException {
		model.addCarPart(part);
	}

	/**
	 * Get the CarOptions of the given CarOptionCategory that can still be chosen for the model
	 * 
	 * @param 	type
	 * 				The VehicleOptionCategory of which the still available VehicleOptions will be returned
	 * 
	 * @return
	 * 				A Collection that contains the still available VehicleOptions of the given VehicleOptionCategory.
	 */
	public List<VehicleOption> getStillAvailableCarParts(
			VehicleOptionCategory type) {
		Collection<VehicleOption> availableParts = new HashSet<>();

		availableParts = checkBindingRestrictions(type);
		availableParts = removeConflictingBindingParts(type, availableParts);
		availableParts = checkOptionalRestrictions(type, availableParts);

		List<VehicleOption> options = new ArrayList<>();
		options.addAll(availableParts);
		return options;
	}

	/**
	 * TODO google optionality en vervang het door een beter woord
	 * Makes sure that the optionality of the correct VehicleOptionCategories is overridden for the current Vehicle, 
	 * according to the OptionalRestrictions.Also makes sure that if a restriction can't be fulfilled anymore due to 
	 * already chosen parts, the VehicleOption in the first part of the restriction can't be chosen anymore. 
	 */
	private Collection<VehicleOption> checkOptionalRestrictions(
			VehicleOptionCategory type, Collection<VehicleOption> availableParts) {

		for (OptionalRestriction restriction : optionalRestrictions) {
			Map<VehicleOptionCategory, VehicleOption> parts = model.getVehicleOptions();
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
	 * Returns the VehicleOptions which can still be chosen after the current Vehicle 
	 * has been checked with all the BindingRestrictions
	 * 
	 * @param 	type
	 * 				The VehicleOptionCategory of which the still available VehicleOptions will be returned
	 * 
	 * @return  The still available VehicleOptions as a Collection.
	 */
	private Collection<VehicleOption> checkBindingRestrictions(
			VehicleOptionCategory type) {
		Set<VehicleOption> availableParts = new HashSet<>();

		for (BindingRestriction restriction : bindingRestrictions) {
			if (model.getVehicleOptions().values()
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
	 * Removes VehicleOptions from the Collection of still available VehicleOptions 
	 * if, due to the already chosen parts in the model,
	 * a BindingRestriction can't be fulfilled anymore. 
	 * (If the second part of the  restriction can't be fulfilled anymore, the VehicleOption 
	 * in the first part is removed from the Collection.)
	 * 
	 * @param 	type
	 * 				The VehicleOptionCategory of which the still available parts will be returned
	 * @param 	availableParts
	 * 				The VehicleOptions that were previously selected to still be available
	 * @return
	 * 				The VehicleOptions that are still available after the right VehicleOptions are 
	 * 				removed from the given Collection
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
				VehicleOption inModel = model.getVehicleOptions().get(option2.getType());
				if (inModel != null && !options.get(option1).contains(inModel)) {
					availableParts.remove(option1);
				}

			}
		}

		return availableParts;
	}

	/**
	 * Get the Vehicle that has been built or is being build by the PartPicker
	 */
	public Vehicle getModel() {
		return model;
	}

	/**
	 * Add a BindingRestriction that the PartPicker has to take into account
	 */
	public void addBindingRestriction(BindingRestriction restriction) {
		bindingRestrictions.add(restriction);

	}

	/**
	 * Add an OptionalRestriction that the PartPicker has to take into account
	 */
	public void addOptionalRestriction(OptionalRestriction restriction) {
		optionalRestrictions.add(restriction);
	}

	/**
	 * Get the BindingRestrictions the PartPicker has to take into account
	 */
	public Set<BindingRestriction> getBindingRestrictions() {
		return Collections.unmodifiableSet(bindingRestrictions);
	}

	/**
	 * Set the BindingRestrictions the PartPicker has to take into account
	 */
	public void setBindingRestrictions(
			Set<BindingRestriction> bindingRestrictions) {
		this.bindingRestrictions = bindingRestrictions;
	}

	/**
	 * Get the OptionalRestrictions the PartPicker has to take into account
	 */
	public Set<OptionalRestriction> getOptionalRestrictions() {
		return Collections.unmodifiableSet(optionalRestrictions);
	}

	/**
	 * Set the OptionalRestrictions the PartPicker has to take into account
	 */
	public void setOptionalRestrictions(
			Set<OptionalRestriction> optionalRestrictions) {
		this.optionalRestrictions = optionalRestrictions;
	}

	public VehicleSpecification getSpecification(String specificationName) {
		VehicleSpecification specification = getCatalogue().getCatalogue().get(specificationName);
		if(specification==null){
			throw new IllegalArgumentException();
		}
		return specification;
	}

	public Set<String> getVehicleSpecifications() {
		return getCatalogue().getCatalogue().keySet();
	}

	public VehicleSpecificationCatalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(VehicleSpecificationCatalogue catalogue) {
		this.catalogue = catalogue;
	}
}
