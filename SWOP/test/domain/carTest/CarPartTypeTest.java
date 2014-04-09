package domain.carTest;

import org.junit.Test;
import static org.junit.Assert.*;

import domain.car.CarOptionCategory;

public class CarPartTypeTest {

	@Test
	public void test() {
		assertNotEquals(CarOptionCategory.AIRCO, CarOptionCategory.BODY);
		assertEquals(CarOptionCategory.AIRCO, CarOptionCategory.valueOf("AIRCO"));
		assertFalse(CarOptionCategory.BODY.isOptional());
		assertTrue(CarOptionCategory.AIRCO.isOptional());
	}

}
