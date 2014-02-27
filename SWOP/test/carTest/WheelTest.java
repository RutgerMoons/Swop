package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Wheel;

public class WheelTest {

	@Test
	public void test() {
		Wheel w1 = new Wheel();
		assertNotNull(w1);
		
		ArrayList<String> types = new ArrayList<>();
		Wheel w2 = new Wheel(types);
		assertNotNull(w2);
	}

}
