package domain.carTest;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.car.CustomCarModel;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

public class CustomCarModelTest {

	CustomCarModel model;
	@Before
	public void initialize(){
		model = new CustomCarModel();
	}

	@Test
	public void testAddCarPart() throws AlreadyInMapException, ImmutableException {
		CarOption sportBody = new CarOption("sport", CarOptionCategory.BODY);
		model.addCarPart(sportBody);
		assertEquals(sportBody, model.getCarParts().get(CarOptionCategory.BODY));
	}
	
	@Test(expected=AlreadyInMapException.class)
	public void testAddInvalidCarparts() throws AlreadyInMapException, ImmutableException {
		model.addCarPart(new CarOption("sport", CarOptionCategory.BODY));
		model.addCarPart(new CarOption("break", CarOptionCategory.BODY));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNullCarPart() throws AlreadyInMapException, ImmutableException{
		model.addCarPart(null);
	}
	
	@Test
	public void testToString() throws AlreadyInMapException, ImmutableException{
		model.addCarPart(new CarOption("sport", CarOptionCategory.BODY));
		String s = System.lineSeparator();
		assertEquals("Custom order:" + s + "BODY: sport", model.toString());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetForcedOptionalTypes() throws NotImplementedException{
		model.getForcedOptionalTypes();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAddForcedOptionalTypes() throws ImmutableException, NotImplementedException{
		model.addForcedOptionalType(new CarOption("sport", CarOptionCategory.BODY), false);
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
