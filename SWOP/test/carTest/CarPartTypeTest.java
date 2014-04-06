package carTest;

import org.junit.Test;
import static org.junit.Assert.*;

import car.CarPartType;

public class CarPartTypeTest {

	@Test
	public void test() {
		assertNotEquals(CarPartType.AIRCO, CarPartType.BODY);
		assertEquals(CarPartType.AIRCO, CarPartType.valueOf("AIRCO"));
	}

}
