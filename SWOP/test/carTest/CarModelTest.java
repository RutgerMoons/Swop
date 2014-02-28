package carTest;

import static org.junit.Assert.*;

import org.junit.Test;

import car.CarDescription;

public class CarModelTest {

	@Test
	public void test() {
		CarDescription carDes = new CarDescription("Description");
		assertNull(carDes);
	}

}
