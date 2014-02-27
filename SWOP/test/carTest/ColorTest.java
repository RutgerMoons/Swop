package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Color;

public class ColorTest {

	@Test
	public void test() {
		Color color = new Color();
		assertNotNull(color);
		
		ArrayList<String> types = new ArrayList<>();
		Color color2 = new Color(types);
		assertNotNull(color2);
		
	}

}
