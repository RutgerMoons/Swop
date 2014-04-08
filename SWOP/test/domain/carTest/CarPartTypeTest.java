package domain.carTest;

import org.junit.Test;
import static org.junit.Assert.*;

import domain.car.CarPartType;

public class CarPartTypeTest {

	@Test
	public void test() {
		assertNotEquals(CarPartType.AIRCO, CarPartType.BODY);
		assertEquals(CarPartType.AIRCO, CarPartType.valueOf("AIRCO"));
	}

	@Test
	public void test2(){
		for(CarPartType type: CarPartType.values())
			System.out.println(type);
	}
}
