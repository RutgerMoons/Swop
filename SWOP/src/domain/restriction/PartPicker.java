package domain.restriction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import domain.car.CarModel;
import domain.car.CarModelTemplate;
import domain.car.CarPart;
import domain.car.CarPartType;
import domain.exception.AlreadyInMapException;

public class PartPicker {
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private CarModel model;

	public PartPicker(CarModelTemplate template) {
		bindingRestrictions = new HashSet<BindingRestriction>();
		optionalRestrictions = new HashSet<OptionalRestriction>();
		model = new CarModel(template);
	}

	public void addCarPartToModel(CarPart part) throws AlreadyInMapException {
		model.addCarPart(part);
	}

	public Collection<CarPart> getStillAvailableCarParts(CarPartType type) {
		Collection<CarPart> availableParts = new HashSet<>();

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

	private Collection<CarPart> checkBindingRestrictions(CarPartType type) {
		Set<CarPart> availableParts = new HashSet<>();
		for (BindingRestriction restriction : bindingRestrictions) {
			if (model.getCarParts().values()
					.contains(restriction.getChosenCarPart())
					&& restriction.getChosenCarPart().getType().equals(type)
					&& model.getTemplate().getCarParts().values()
							.contains(restriction.getRestrictedCarPart())) {
				availableParts.add(restriction.getRestrictedCarPart());
			}
		}
		if (availableParts.isEmpty())
			return model.getTemplate().getCarParts().get(type);
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
