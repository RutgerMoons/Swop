package domain.vehicleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class VehicleOptionCategoryTest {

	@Test
	public void test() {
		assertNotEquals(VehicleOptionCategory.AIRCO, VehicleOptionCategory.BODY);
		assertEquals(VehicleOptionCategory.AIRCO, VehicleOptionCategory.valueOf("AIRCO"));
		assertFalse(VehicleOptionCategory.BODY.isOptional());
		assertTrue(VehicleOptionCategory.AIRCO.isOptional());
	}

}