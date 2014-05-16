<<<<<<< HEAD
package domain.vehicleTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
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
	public void testAddCarPart() throws AlreadyInMapException, UnmodifiableException {
		VehicleOption sportBody = new VehicleOption("sport", VehicleOptionCategory.BODY);
		model.addVehicleOption(sportBody);
		assertEquals(sportBody, model.getVehicleOptions().get(VehicleOptionCategory.BODY));
	}
	
	@Test(expected=AlreadyInMapException.class)
	public void testAddInvalidCarparts() throws AlreadyInMapException, UnmodifiableException {
		model.addVehicleOption(new VehicleOption("sport", VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("break", VehicleOptionCategory.BODY));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNullCarPart() throws AlreadyInMapException, UnmodifiableException{
		model.addVehicleOption(null);
	}
	
	@Test
	public void testToString() throws AlreadyInMapException, UnmodifiableException{
		model.addVehicleOption(new VehicleOption("sport", VehicleOptionCategory.BODY));
		assertEquals("Body: sport", model.toString());
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
		model.getVehicleSpecification();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testsetSpecification() throws UnmodifiableException, NotImplementedException{
		model.setVehicleSpecification(null);
	}
}
=======
package domain.vehicleTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
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
	public void testAddCarPart() throws AlreadyInMapException, UnmodifiableException {
		VehicleOption sportBody = new VehicleOption("sport", VehicleOptionCategory.BODY);
		model.addCarPart(sportBody);
		assertEquals(sportBody, model.getVehicleOptions().get(VehicleOptionCategory.BODY));
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
		assertEquals("Body: sport", model.toString());
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
>>>>>>> 0fbc9f8f29bcd199f2bc75d53ce8160004a0229f
