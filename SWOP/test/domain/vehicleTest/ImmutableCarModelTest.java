package domain.vehicleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;
import domain.vehicle.IVehicle;
import domain.vehicle.UnmodifiableVehicle;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

public class ImmutableCarModelTest {

	private IVehicle car;
	private IVehicle immutable;
	@Before
	public void initialize(){
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		car = new Vehicle(template);
		immutable = new UnmodifiableVehicle(car);
	}
	
	
	@Test
	public void testGetters() throws AlreadyInMapException, UnmodifiableException, NotImplementedException {
		car.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		car.addCarPart(new VehicleOption("break", VehicleOptionCategory.BODY));
		
		car.addForcedOptionalType(new VehicleOption("sport", VehicleOptionCategory.BODY), false);
		
		assertEquals(car.getCarParts(), immutable.getCarParts());
		assertEquals(car.toString(), immutable.toString());
		assertTrue(immutable.equals(car));
		assertEquals(car.hashCode(), immutable.hashCode());
		
		assertEquals(car.getSpecification(), immutable.getSpecification());
		
		assertFalse(immutable.getForcedOptionalTypes().get(new VehicleOption("sport", VehicleOptionCategory.BODY)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor(){
		new UnmodifiableVehicle(null);
	}
	
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable1() throws AlreadyInMapException, UnmodifiableException{
		immutable.addCarPart(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable2() throws UnmodifiableException, NotImplementedException{
		immutable.addForcedOptionalType(null, true);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable3() throws UnmodifiableException, NotImplementedException{
		immutable.setSpecification(null);
	}
	
	
}