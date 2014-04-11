package domain.restriction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.AlreadyInMapException;

/**
 * A PartPicker has all the tools to compose a CarModel.
 */
public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private CarModel model;

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
	 * Creates a new CarModel that will be built according to the template, by this PartPicker.
	 * @param template
	 * 			The CarModelSpecification according to which this CarModel will be built.
	 */
	public void setNewModel(CarModelSpecification template) {
		for (OptionalRestriction restriction : optionalRestrictions) {
			restriction.setRestrictedPartAlreadyChosen(false);
		}
		model = new CarModel(template);
	}

	/**
	 * Adds a CarOption to the CarModel this PartPicker is currently building.
	 * @param part
	 * 			The CarOption to be added to the model.
	 * @throws AlreadyInMapException
	 * 			If the CarModel already contains a CarOption of the same type.
	 */
	public void addCarPartToModel(CarOption part) throws AlreadyInMapException {
		model.addCarPart(part);
	}

	/**
	 * Get the CarOptions of the given CarOptionCategory that can still be chosen for the model.
	 * @param type
	 * 			The CarOptionCategory of which the still available CarOptions will be returned.
	 * @return
	 * 		A Collection that contains the still available CarOptions of the given CarOptionCategory.
	 */
	public Collection<CarOption> getStillAvailableCarParts(
			CarOptionCategory type) {
		Collection<CarOption> availableParts = new HashSet<>();

		availableParts = checkBindingRestrictions(type);
		availableParts = removeConflictingBindingParts(type, availableParts);
		availableParts = checkOptionalRestrictions(type, availableParts);

		return availableParts;
	}

	/**
	 * 
	 * @param type
	 * @param availableParts
	 * @return
	 */
	private Collection<CarOption> checkOptionalRestrictions(
			CarOptionCategory type, Collection<CarOption> availableParts) {

		for (OptionalRestriction restriction : optionalRestrictions) {
			Map<CarOptionCategory, CarOption> parts = model.getCarParts();
			if (restriction.getCarPart().getType().equals(type)) {				//de availableparts die teruggegeven worden zijn om een CarPart te kiezen van hetzelfde type als het CarPart dat in het eerste deel van de restrictie staat
				if (!restriction.getRestrictedPartAlreadyChosen()) {
					model.addForcedOptionalType(restriction.getCarPartType(),
							restriction.isOptional());
				} else if (restriction.getRestrictedPartAlreadyChosen()			//als het 2e part uit de restriction al gekozen is, maar het zit niet in het model
						&& !parts.containsKey(restriction.getCarPartType())) {  // ==> user heeft 'optional' gekozen
					availableParts.remove(restriction.getCarPart());			//   ==> er kan niet meer aan de restrictie voldaan worden, dus het 1e part moet uit de availableParts verwijderd worden
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
	private Collection<CarOption> checkBindingRestrictions(
			CarOptionCategory type) {
		Set<CarOption> availableParts = new HashSet<>();
		
		for (BindingRestriction restriction : bindingRestrictions) {
			if (model.getCarParts().values()
					.contains(restriction.getChosenCarPart())
					&& restriction.getRestrictedCarPart().getType().equals(type)
					&& model.getSpecification().getCarParts().values()
							.contains(restriction.getRestrictedCarPart())) {
				availableParts.add(restriction.getRestrictedCarPart());
			}
		}

		if (availableParts.isEmpty())
			return model.getSpecification().getCarParts().get(type);
		return availableParts;
	}

	/**
	 * Removes CarOptions from the Collection of still available CarOptions 
	 * if, due to the already chosen parts in the model,
	 * a BindingRestriction can't be fulfilled anymore. 
	 * (If the second part of the  restriction can't be fulfilled anymore, the CarOption in the first part is removed from te Collection.)
	 * @param type
	 * 		The CarOptionCategory of which the still available parts will be returned.
	 * @param availableParts
	 * 		The CarOptions that were previously selected to still be available.
	 * @return
	 * 		The CarOptions that are still available after the right CarOptions are removed from the given Collection.
	 */
	private Collection<CarOption> removeConflictingBindingParts(
			CarOptionCategory type, Collection<CarOption> availableParts) {

		Multimap<CarOption, CarOption> options = HashMultimap.create();
		for (BindingRestriction restriction : bindingRestrictions) {
			if (restriction.getChosenCarPart().getType().equals(type)) {
				options.put(restriction.getChosenCarPart(),
						restriction.getRestrictedCarPart());
			}
		}

		for (CarOption option1 : options.keySet()) {
			for (CarOption option2 : options.get(option1)) {
				CarOption inModel = model.getCarParts().get(option2.getType());
				if (inModel != null && !options.get(option1).contains(inModel)) {
					availableParts.remove(option1);
				}

			}
		}

		return availableParts;
	}

	public CarModel getModel() {
		return model;
	}

	public void addBindingRestriction(BindingRestriction restriction) {
		bindingRestrictions.add(restriction);

	}

	public void addOptionalRestriction(OptionalRestriction restriction) {
		optionalRestrictions.add(restriction);
	}

	public Set<BindingRestriction> getBindingRestrictions() {
		return bindingRestrictions;
	}

	public void setBindingRestrictions(
			Set<BindingRestriction> bindingRestrictions) {
		this.bindingRestrictions = bindingRestrictions;
	}

	public Set<OptionalRestriction> getOptionalRestrictions() {
		return optionalRestrictions;
	}

	public void setOptionalRestrictions(
			Set<OptionalRestriction> optionalRestrictions) {
		this.optionalRestrictions = optionalRestrictions;
	}

}
