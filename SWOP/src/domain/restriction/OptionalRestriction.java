package domain.restriction;

import domain.car.CarPart;
import domain.car.CarPartType;


public class OptionalRestriction{
	
	private CarPartType type;
	private Boolean optional;
	private CarPart part;

	public OptionalRestriction(CarPart part, CarPartType type, Boolean optional){
		this.type = type;
		this.optional = optional;
		this.part = part;
	}
	
	public CarPart getCarPart(){
		return part;
	}
	
	public CarPartType getCarPartType(){
		return type;
	}
	
	public boolean isOptional(){
		return optional;
	}
	
}
