package domain.restriction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategogry;
import domain.exception.AlreadyInMapException;

public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private CarModel model;

	public PartPicker() {
		bindingRestrictions = new HashSet<BindingRestriction>();
		optionalRestrictions = new HashSet<OptionalRestriction>();
	}
	
	public void setNewModel(CarModelSpecification template){
		model = new CarModel(template);
	}

	public void addCarPartToModel(CarOption part) throws AlreadyInMapException {
		model.addCarPart(part);
	}

	public Collection<CarOption> getStillAvailableCarParts(CarOptionCategogry type) {
		Collection<CarOption> availableParts = new HashSet<>();

		availableParts = checkBindingRestrictions(type);
		checkOptionalRestrictions();

		return availableParts;
	}

	private void checkOptionalRestrictions() {
		for (OptionalRestriction restriction : optionalRestrictions) {
			if (model.getCarParts().get(restriction.getCarPartType())
					.equals(restriction.getCarPart())) {
				model.addForcedOptionalType(restriction.getCarPartType(),
						restriction.isOptional());
			}
		}

	}

	private Collection<CarOption> checkBindingRestrictions(CarOptionCategogry type) {
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
		if (availableParts.isEmpty())
			return model.getSpecification().getCarParts().get(type);
		return availableParts;
	}
	

	public CarModel getModel() {
		return model;
	}
	
	public void addBindingRestriction(BindingRestriction restriction){
		bindingRestrictions.add(restriction);
		
	}
	
	public void addOptionalRestriction(OptionalRestriction restriction){
		optionalRestrictions.add(restriction);
	}
	
	public Set<BindingRestriction> getBindingRestrictions() {
		return bindingRestrictions;
	}

	public void setBindingRestrictions(Set<BindingRestriction> bindingRestrictions) {
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
