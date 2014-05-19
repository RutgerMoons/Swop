package domain.vehicle.vehicleTest;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class CustomVehicleTest {

	CustomVehicle model;
	@Before
	public void initialize(){
		model = new CustomVehicle();
	}

	@Test
	public void testAddCarPart() {
		VehicleOption sportBody = new VehicleOption("sport", VehicleOptionCategory.BODY);
		model.addVehicleOption(sportBody);
		assertEquals(sportBody, model.getVehicleOptions().get(VehicleOptionCategory.BODY));
	}
	
	@Test(expected=AlreadyInMapException.class)
	public void testAddInvalidCarparts() {
		model.addVehicleOption(new VehicleOption("sport", VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("break", VehicleOptionCategory.BODY));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNullCarPart() {
		model.addVehicleOption(null);
	}
	
	@Test
	public void testToString() {
		model.addVehicleOption(new VehicleOption("sport", VehicleOptionCategory.BODY));
		assertEquals("Body: sport", model.toString());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetForcedOptionalTypes() {
		model.getForcedOptionalTypes();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAddForcedOptionalTypes() {
		model.addForcedOptionalType(new VehicleOption("sport", VehicleOptionCategory.BODY), false);
	}
	
	@Test
	public void testGetSpecification() {
		assertEquals("custom", model.getVehicleSpecification().getDescription());
	}
	
	@Test
	public void testSetSpecification(){
		VehicleSpecification specification = new VehicleSpecification("test", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		model.setVehicleSpecification(specification);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetIllegalSpecification() {
		model.setVehicleSpecification(null);
	}
	
	@Test
	public void testGetTimeAtWorkBench(){
		assertFalse(model.getTimeAtWorkBench().containsValue(60));
		model.addVehicleOption(new VehicleOption("test", VehicleOptionCategory.BODY));
		Integer test = model.getTimeAtWorkBench().get(WorkBenchType.BODY);
		assertEquals(new Integer(60), test);
	}
	
	@Test
	public void testEquals(){
		assertEquals(model, model);
		CustomVehicle vehicle = new CustomVehicle();
		assertEquals(model, vehicle);
		assertEquals(model.hashCode(), vehicle.hashCode());
		assertNotEquals(model, VehicleOptionCategory.AIRCO);
		
		
		CustomVehicle vehicle2 = new CustomVehicle();
		VehicleSpecification specification = new VehicleSpecification("test", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		vehicle2.setVehicleSpecification(specification);
		assertNotEquals(model, vehicle2);
		assertNotEquals(model.hashCode(), vehicle2.hashCode());
		
		CustomVehicle vehicle3 = new CustomVehicle();
		vehicle3.addVehicleOption(new VehicleOption("test", VehicleOptionCategory.AIRCO));
		assertNotEquals(model, vehicle3);
		assertNotEquals(model.hashCode(), vehicle3.hashCode());
		
		assertFalse(model.equals(null));
		
		
	}
}
