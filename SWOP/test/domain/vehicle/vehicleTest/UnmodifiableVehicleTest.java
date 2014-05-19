package domain.vehicle.vehicleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.UnmodifiableVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class UnmodifiableVehicleTest {

	private IVehicle car;
	private IVehicle immutable;
	@Before
	public void initialize(){
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		car = new Vehicle(template);
		immutable = new UnmodifiableVehicle(car);
	}
	
	
	@Test
	public void testGetters() throws AlreadyInMapException, UnmodifiableException, NotImplementedException {
		car.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		car.addVehicleOption(new VehicleOption("break", VehicleOptionCategory.BODY));
		
		car.addForcedOptionalType(new VehicleOption("sport", VehicleOptionCategory.BODY), false);
		
		assertEquals(car.getVehicleOptions(), immutable.getVehicleOptions());
		assertEquals(car.toString(), immutable.toString());
		assertTrue(immutable.equals(car));
		//TODO
		assertTrue(car.equals(immutable));
		assertEquals(car.hashCode(), immutable.hashCode());
		
		assertEquals(car.getVehicleSpecification(), immutable.getVehicleSpecification());
		
		
		assertFalse(immutable.getForcedOptionalTypes().get(new VehicleOption("sport", VehicleOptionCategory.BODY)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor(){
		new UnmodifiableVehicle(null);
	}
	
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable1() throws AlreadyInMapException, UnmodifiableException{
		immutable.addVehicleOption(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable2() throws UnmodifiableException, NotImplementedException{
		immutable.addForcedOptionalType(null, true);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable3() throws UnmodifiableException, NotImplementedException{
		immutable.setVehicleSpecification(null);
	}
	
	@Test
	public void testGetTimeAtWorkBench(){
		assertEquals(car.getTimeAtWorkBench(), immutable.getTimeAtWorkBench());
	}
	
}
