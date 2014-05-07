package domain.carTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.car.VehicleOption;
import domain.car.VehicleOptionCategory;
import domain.car.CustomVehicle;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

public class CustomCarModelTest {

	CustomVehicle model;
	@Before
	public void initialize(){
		model = new CustomVehicle();
	}

	@Test
	public void testAddCarPart() throws AlreadyInMapException, ImmutableException {
		VehicleOption sportBody = new VehicleOption("sport", VehicleOptionCategory.BODY);
		model.addCarPart(sportBody);
		assertEquals(sportBody, model.getCarParts().get(VehicleOptionCategory.BODY));
	}
	
	@Test(expected=AlreadyInMapException.class)
	public void testAddInvalidCarparts() throws AlreadyInMapException, ImmutableException {
		model.addCarPart(new VehicleOption("sport", VehicleOptionCategory.BODY));
		model.addCarPart(new VehicleOption("break", VehicleOptionCategory.BODY));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNullCarPart() throws AlreadyInMapException, ImmutableException{
		model.addCarPart(null);
	}
	
	@Test
	public void testToString() throws AlreadyInMapException, ImmutableException{
		model.addCarPart(new VehicleOption("sport", VehicleOptionCategory.BODY));
		String s = System.lineSeparator();
		assertEquals("BODY: sport", model.toString());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetForcedOptionalTypes() throws NotImplementedException{
		model.getForcedOptionalTypes();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAddForcedOptionalTypes() throws ImmutableException, NotImplementedException{
		model.addForcedOptionalType(new VehicleOption("sport", VehicleOptionCategory.BODY), false);
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetSpecification() throws NotImplementedException{
		model.getSpecification();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testsetSpecification() throws ImmutableException, NotImplementedException{
		model.setSpecification(null);
	}
}
