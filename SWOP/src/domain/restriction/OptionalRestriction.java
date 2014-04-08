package domain.restriction;

import domain.car.CarOption;
import domain.car.CarOptionCategogry;


public class OptionalRestriction{
	
	private CarOptionCategogry type;
	private Boolean optional;
	private CarOption part;

	public OptionalRestriction(CarOption part, CarOptionCategogry type, Boolean optional){
		this.type = type;
		this.optional = optional;
		this.part = part;
	}
	
	public CarOption getCarPart(){
		return part;
	}
	
	public CarOptionCategogry getCarPartType(){
		return type;
	}
	
	public boolean isOptional(){
		return optional;
	}
	
}
