package domain.vehicleTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
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
	
	public void testGetSpecification() {
		assertEquals("custom", model.getVehicleSpecification().getDescription());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testsetSpecification() {
		model.setVehicleSpecification(null);
	}
}
