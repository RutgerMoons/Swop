package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Gearbox;


public class GearboxTest {

	@Test
	public void test() {
		Gearbox g = new Gearbox();
		assertNotNull(g);
		
		ArrayList<String> types = new ArrayList<>();
		Gearbox g2 = new Gearbox(types);
		assertNotNull(g2);
	}

}
