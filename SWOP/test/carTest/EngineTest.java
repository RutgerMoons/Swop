package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Engine;

public class EngineTest {

	@Test
	public void test() {
		Engine engine = new Engine();
		assertNotNull(engine);
		
		ArrayList<String> types = new ArrayList<>();
		types.add("performance");
		Engine engine2 = new Engine(types);
		assertNotNull(engine2);
	}

}
