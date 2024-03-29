package domain.restrictionTest;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class RestrictionTest {
	PartPicker picker;
	@Before
	public void initialize(){
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		
		VehicleSpecificationCatalogue catalogue = new VehicleSpecificationCatalogue();
		catalogue.addModel(new VehicleSpecification("model A", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>()));
		picker = new PartPicker(catalogue, bindingRestrictions, optionalRestrictions);
		
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
	
		parts.add(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("leather white", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("sports", VehicleOptionCategory.WHEEL));
		
		parts.add(new VehicleOption("high", VehicleOptionCategory.SPOILER));
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		Set<VehicleOption> obligatory = new HashSet<VehicleOption>();
		obligatory.add(new VehicleOption("Cargo", VehicleOptionCategory.CARGO));
		obligatory.add(new VehicleOption("Certification", VehicleOptionCategory.CERTIFICATION));
		VehicleSpecification template = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>(), obligatory);
		picker.setNewModel(template);
		
	}
	
	@Test
	public void testContstructor(){
		assertNotNull(picker.getBindingRestrictions());
		assertNotNull(picker.getOptionalRestrictions());
		assertNotNull(picker.getVehicleSpecificationCatalogue());
		assertTrue(picker.getModel().getVehicleOptions().containsValue(new VehicleOption("Cargo", VehicleOptionCategory.CARGO)));
		assertTrue(picker.getModel().getVehicleOptions().containsValue(new VehicleOption("Certification", VehicleOptionCategory.CERTIFICATION)));
	}

	@Test
	public void testBodyToSpoiler() throws AlreadyInMapException {
		VehicleOption sport = new VehicleOption("sport", VehicleOptionCategory.BODY);
		picker.addCarPartToModel(sport);
		picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);
		
		picker.getStillAvailableCarParts(VehicleOptionCategory.SPOILER);
		
		assertFalse(picker.getModel().getForcedOptionalTypes().get(sport));
		
	}
	
	@Test
	public void testSpoilerToBody1() throws AlreadyInMapException {
		picker.getStillAvailableCarParts(VehicleOptionCategory.SPOILER);
		
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);
		
		assertFalse(available.contains(new VehicleOption("sport", VehicleOptionCategory.BODY)));
	}

	@Test
	public void testSpoilerToBody2() throws AlreadyInMapException {
		picker.getStillAvailableCarParts(VehicleOptionCategory.SPOILER);
		picker.addCarPartToModel(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);
		
		assertTrue(available.contains(new VehicleOption("sport", VehicleOptionCategory.BODY)));
	}
	
	
	@Test
	public void testSportToEngines() throws AlreadyInMapException{
		picker.addCarPartToModel(new VehicleOption("sport", VehicleOptionCategory.BODY));
		picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.ENGINE);
		
		assertTrue(available.contains(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		assertTrue(available.contains(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		assertFalse(available.contains(new VehicleOption("standard 2l V4", VehicleOptionCategory.ENGINE)));
	}
	
	
	@Test
	public void testEngineNoSport() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new VehicleOption("standard 2l V4", VehicleOptionCategory.ENGINE));
		picker.getStillAvailableCarParts(VehicleOptionCategory.ENGINE);
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);
		
		assertFalse(available.contains(new VehicleOption("sport", VehicleOptionCategory.BODY)));
		
	}
	
	@Test
	public void testCorrectEngineToSport() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		picker.getStillAvailableCarParts(VehicleOptionCategory.ENGINE);
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);
		
		assertTrue(available.contains(new VehicleOption("sport", VehicleOptionCategory.BODY)));
		
	}
	
	@Test
	public void testUltraManualAirco() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
		picker.getStillAvailableCarParts(VehicleOptionCategory.ENGINE);
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.AIRCO);
		
		assertTrue(available.contains(new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		assertFalse(available.contains(new VehicleOption("automatic", VehicleOptionCategory.AIRCO)));
		
	}
	
	@Test
	public void testAutomaticAircoNoUltra() throws AlreadyInMapException{
		
		picker.addCarPartToModel(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		picker.getStillAvailableCarParts(VehicleOptionCategory.AIRCO);
		Collection<VehicleOption> available = picker.getStillAvailableCarParts(VehicleOptionCategory.ENGINE);
		
		assertFalse(available.contains(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
	}
	
	@Test
	public void testBodyBreakNoSpoiler() throws AlreadyInMapException{
		VehicleOption breakBody = new VehicleOption("break", VehicleOptionCategory.BODY);
		picker.addCarPartToModel(breakBody);
		picker.getStillAvailableCarParts(VehicleOptionCategory.BODY);

		assertNull(picker.getModel().getForcedOptionalTypes().get(breakBody));
		
	}
	
	
	@Test
	public void testAdd(){
		picker = new PartPicker(new VehicleSpecificationCatalogue(), new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>());
		picker.addBindingRestriction(new BindingRestriction(new VehicleOption("blabla", VehicleOptionCategory.AIRCO), new VehicleOption("test", VehicleOptionCategory.BODY)));
		picker.addOptionalRestriction(new OptionalRestriction(new VehicleOption("blabla", VehicleOptionCategory.ENGINE), VehicleOptionCategory.SPOILER, false));
		
		assertEquals(1, picker.getBindingRestrictions().size());
		assertEquals(1, picker.getOptionalRestrictions().size());
	}
	
	@Test
	public void testGetSpecification(){
		assertNotNull(picker.getSpecification("model A"));
		assertNotNull(picker.getVehicleSpecifications());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetIllegalSpecification(){
		picker.getSpecification(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalBindingRestriction1(){
		new BindingRestriction(null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalBindingRestriction2(){
		new BindingRestriction(new VehicleOption("test", VehicleOptionCategory.AIRCO), null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalOptionalRestriction1(){
		new OptionalRestriction(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalOptionalRestriction2(){
		new OptionalRestriction(new VehicleOption("test", VehicleOptionCategory.AIRCO), null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalOptionalRestriction3(){
		new OptionalRestriction(new VehicleOption("test", VehicleOptionCategory.AIRCO), VehicleOptionCategory.AIRCO, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalPartPicker1(){
		new PartPicker(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalPartPicker2(){
		new PartPicker(new VehicleSpecificationCatalogue(), null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalPartPicker3(){
		new PartPicker(new VehicleSpecificationCatalogue(), new HashSet<BindingRestriction>(), null);
	}
	
}
