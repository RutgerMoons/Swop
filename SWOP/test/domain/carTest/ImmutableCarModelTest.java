package domain.carTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategogry;
import domain.car.ICarModel;
import domain.car.ImmutableCarModel;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;

public class ImmutableCarModelTest {

	private ICarModel car;
	private ICarModel immutable;
	@Before
	public void initialize(){
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategogry.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		car = new CarModel(template);
		immutable = new ImmutableCarModel(car);
	}
	
	
	@Test
	public void testGetters() throws AlreadyInMapException, ImmutableException {
		car.addCarPart(new CarOption("manual", CarOptionCategogry.AIRCO));
		car.addCarPart(new CarOption("break", CarOptionCategogry.BODY));
		
		car.addForcedOptionalType(CarOptionCategogry.AIRCO, false);
		
		assertEquals(car.getCarParts(), immutable.getCarParts());
		assertEquals(car.toString(), immutable.toString());
		assertTrue(immutable.equals(car));
		assertEquals(car.hashCode(), immutable.hashCode());
		
		assertEquals(car.getSpecification(), immutable.getSpecification());
		
		assertFalse(immutable.getForcedOptionalTypes().get(CarOptionCategogry.AIRCO));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor(){
		new ImmutableCarModel(null);
	}
	
	
	@Test(expected=ImmutableException.class)
	public void testImmutable1() throws AlreadyInMapException, ImmutableException{
		immutable.addCarPart(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.addForcedOptionalType(null, true);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable3() throws ImmutableException{
		immutable.setSpecification(null);
	}
	
	
}
