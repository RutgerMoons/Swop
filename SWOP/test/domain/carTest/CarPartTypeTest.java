package domain.carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
