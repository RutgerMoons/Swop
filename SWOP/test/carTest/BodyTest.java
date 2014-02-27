package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Body;

public class BodyTest {

	@Test
	public void test() {
		Body body = new Body();
		assertNotNull(body);
		
		ArrayList<String> types = new ArrayList<String>();
		types.add("break");
		types.add("sedan");
		Body body2 = new Body(types);
		assertNotNull(body2);
	}

}
