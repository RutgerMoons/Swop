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

public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private CarModel model;

	public PartPicker(Set<BindingRestriction> bindingRestrictions,
			Set<OptionalRestriction> optionalRestrictions) {
		setBindingRestrictions(bindingRestrictions);
		setOptionalRestrictions(optionalRestrictions);
	}

	public void setNewModel(CarModelSpecification template) {
		for (OptionalRestriction restriction : optionalRestrictions) {
			restriction.setRestrictedPartAlreadyChosen(false);
		}
		model = new CarModel(template);
	}

	public void addCarPartToModel(CarOption part) throws AlreadyInMapException {
		model.addCarPart(part);
	}

	public Collection<CarOption> getStillAvailableCarParts(
			CarOptionCategory type) {
		Collection<CarOption> availableParts = new HashSet<>();

		availableParts = checkBindingRestrictions(type);
		availableParts = removeConflictingBindingParts(type, availableParts);
		availableParts = checkOptionalRestrictions(type, availableParts);

		return availableParts;
	}

	private Collection<CarOption> checkOptionalRestrictions(
			CarOptionCategory type, Collection<CarOption> availableParts) {

		for (OptionalRestriction restriction : optionalRestrictions) {
			Map<CarOptionCategory, CarOption> parts = model.getCarParts();
			if (restriction.getCarPart().getType().equals(type)) {
				if (!restriction.getRestrictedPartAlreadyChosen()) {
					model.addForcedOptionalType(restriction.getCarPartType(),
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
