package domain.carTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;
import domain.vehicle.CustomVehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;

public class CustomCarModelTest {

	CustomVehicle model;
	@Before
	public void initialize(){
		model = new CustomVehicle();
	}

	@Test
	public void testAddCarPart() throws AlreadyInMapException, UnmodifiableException {
		VehicleOption sportBody = new VehicleOption("sport", VehicleOptionCategory.BODY);
		model.addCarPart(sportBody);
		assertEquals(sportBody, model.getCarParts().get(VehicleOptionCategory.BODY));
	}
	
	@Test(expected=AlreadyInMapException.class)
	public void testAddInvalidCarparts() throws AlreadyInMapException, UnmodifiableException {
		model.addCarPart(new VehicleOption("sport", VehicleOptionCategory.BODY));
		model.addCarPart(new VehicleOption("break", VehicleOptionCategory.BODY));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNullCarPart() throws AlreadyInMapException, UnmodifiableException{
		model.addCarPart(null);
	}
	
	@Test
	public void testToString() throws AlreadyInMapException, UnmodifiableException{
		model.addCarPart(new VehicleOption("sport", VehicleOptionCategory.BODY));
		String s = System.lineSeparator();
		assertEquals("BODY: sport", model.toString());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetForcedOptionalTypes() throws NotImplementedException{
		model.getForcedOptionalTypes();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAddForcedOptionalTypes() throws UnmodifiableException, NotImplementedException{
		model.addForcedOptionalType(new VehicleOption("sport", VehicleOptionCategory.BODY), false);
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetSpecification() throws NotImplementedException{
		model.getSpecification();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testsetSpecification() throws UnmodifiableException, NotImplementedException{
		model.setSpecification(null);
	}
}
