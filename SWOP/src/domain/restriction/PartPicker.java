package domain.restriction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.AlreadyInMapException;

public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private CarModel model;

	public PartPicker() {
		bindingRestrictions = new HashSet<BindingRestriction>();
		optionalRestrictions = new HashSet<OptionalRestriction>();
	}

	public void setNewModel(CarModelSpecification template) {
		for(OptionalRestriction restriction: optionalRestrictions){
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
		checkOptionalRestrictions(type);

		return availableParts;
	}

	private void checkOptionalRestrictions(CarOptionCategory type) {

		for (OptionalRestriction restriction : optionalRestrictions) {
			Map<CarOptionCategory, CarOption> parts = model.getCarParts();
			if (parts.get(restriction.getCarPartType()).equals(
					restriction.getCarPart())) {
				if (restriction.getRestrictedPartAlreadyChosen()
						&& !parts.containsKey(restriction.getCarPartType())) {
					model.addForcedOptionalType(restriction.getCarPartType(),
							restriction.isOptional());
				}
			} else if (restriction.getCarPartType().equals(type)
					&& !restriction.isOptional()) {
				restriction.setRestrictedPartAlreadyChosen(true);
			}
		}
	}

	private Collection<CarOption> checkBindingRestrictions(
			CarOptionCategory type) {
		Set<CarOption> availableParts = new HashSet<>();
		for (BindingRestriction restriction : bindingRestrictions) {
			if (model.getCarParts().values()
					.contains(restriction.getChosenCarPart())
					&& restriction.getChosenCarPart().getType().equals(type)
					&& model.getSpecification().getCarParts().values()
							.contains(restriction.getRestrictedCarPart())) {
				availableParts.add(restriction.getRestrictedCarPart());
			}
		}

		availableParts = removeConflictingBindingParts(type, availableParts);

		if (availableParts.isEmpty())
			return model.getSpecification().getCarParts().get(type);
		return availableParts;
	}

	private Set<CarOption> removeConflictingBindingParts(
			CarOptionCategory type, Set<CarOption> availableParts) {

		for (BindingRestriction restriction : bindingRestrictions) {
			CarOption part = model.getCarParts().get(
					restriction.getRestrictedCarPart().getType());

			if (part != null
					&& !part.equals(restriction.getRestrictedCarPart()))
				availableParts.remove(part);
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
