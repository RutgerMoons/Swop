package domain.restrictionTest;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.AlreadyInMapException;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;

public class RestrictionTest {
	PartPicker picker;
	@Before
	public void initialize(){
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new CarOption("sport", CarOptionCategory.BODY), CarOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE), new CarOption("manual", CarOptionCategory.AIRCO)));
		
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);
		
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("leather white", CarOptionCategory.SEATS));
		parts.add(new CarOption("leather black", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategory.SPOILER));
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		picker.setNewModel(template);
		
	}

	@Test
	public void testBodyToSpoiler() throws AlreadyInMapException {
		picker.addCarPartToModel(new CarOption("sport", CarOptionCategory.BODY));
		picker.getStillAvailableCarParts(CarOptionCategory.BODY);
		
		picker.getStillAvailableCarParts(CarOptionCategory.SPOILER);
		
		assertFalse(picker.getModel().getForcedOptionalTypes().get(CarOptionCategory.SPOILER));
		
	}
	
	@Test
	public void testSpoilerToBody1() throws AlreadyInMapException {
		picker.getStillAvailableCarParts(CarOptionCategory.SPOILER);
		
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.BODY);
		
		assertFalse(available.contains(new CarOption("sport", CarOptionCategory.BODY)));
	}

	@Test
	public void testSpoilerToBody2() throws AlreadyInMapException {
		picker.getStillAvailableCarParts(CarOptionCategory.SPOILER);
		picker.addCarPartToModel(new CarOption("low", CarOptionCategory.SPOILER));
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.BODY);
		
		assertTrue(available.contains(new CarOption("sport", CarOptionCategory.BODY)));
	}
	
	
	@Test
	public void testSportToEngines() throws AlreadyInMapException{
		picker.addCarPartToModel(new CarOption("sport", CarOptionCategory.BODY));
		picker.getStillAvailableCarParts(CarOptionCategory.BODY);
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.ENGINE);
		
		assertTrue(available.contains(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE)));
		assertTrue(available.contains(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		assertFalse(available.contains(new CarOption("standard 2l V4", CarOptionCategory.ENGINE)));
	}
	
	
	@Test
	public void testEngineNoSport() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new CarOption("standard 2l V4", CarOptionCategory.ENGINE));
		picker.getStillAvailableCarParts(CarOptionCategory.ENGINE);
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.BODY);
		
		assertFalse(available.contains(new CarOption("sport", CarOptionCategory.BODY)));
		
	}
	
	@Test
	public void testCorrectEngineToSport() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		picker.getStillAvailableCarParts(CarOptionCategory.ENGINE);
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.BODY);
		
		assertTrue(available.contains(new CarOption("sport", CarOptionCategory.BODY)));
		
	}
	
	@Test
	public void testUltraManualAirco() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
		picker.getStillAvailableCarParts(CarOptionCategory.ENGINE);
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.AIRCO);
		
		assertTrue(available.contains(new CarOption("manual", CarOptionCategory.AIRCO)));
		assertFalse(available.contains(new CarOption("automatic", CarOptionCategory.AIRCO)));
		
	}
	
	@Test
	public void testAutomaticAircoNoUltra() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new CarOption("automatic", CarOptionCategory.AIRCO));
		picker.getStillAvailableCarParts(CarOptionCategory.AIRCO);
		Collection<CarOption> available = picker.getStillAvailableCarParts(CarOptionCategory.ENGINE);
		
		assertFalse(available.contains(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		
	}
	
	
	@Test
	public void testAdd(){
		picker = new PartPicker(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>());
		picker.addBindingRestriction(new BindingRestriction(new CarOption("blabla", CarOptionCategory.AIRCO), new CarOption("test", CarOptionCategory.BODY)));
		picker.addOptionalRestriction(new OptionalRestriction(new CarOption("blabla", CarOptionCategory.ENGINE), CarOptionCategory.SPOILER, false));
		
		assertEquals(1, picker.getBindingRestrictions().size());
		assertEquals(1, picker.getOptionalRestrictions().size());
	}
}
