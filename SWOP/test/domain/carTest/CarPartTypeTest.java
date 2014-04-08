package domain.carTest;

import org.junit.Test;
import static org.junit.Assert.*;

import domain.car.CarOptionCategogry;

public class CarPartTypeTest {

	@Test
	public void test() {
		assertNotEquals(CarOptionCategogry.AIRCO, CarOptionCategogry.BODY);
		assertEquals(CarOptionCategogry.AIRCO, CarOptionCategogry.valueOf("AIRCO"));
		assertFalse(CarOptionCategogry.BODY.isOptional());
		assertTrue(CarOptionCategogry.AIRCO.isOptional());
	}

}
