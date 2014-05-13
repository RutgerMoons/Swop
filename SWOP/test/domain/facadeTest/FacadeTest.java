package domain.facadeTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.users.AccessRight;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

public class FacadeTest {

	private Facade facade;
	
	@Before
	public void initialize() {
		//TODO company initializen
		facade = new Facade(null);
	}
	
	
	@Test
	public void teststartNewDay() {
		assertNotNull(facade);
		facade.startNewDay();
		assertNotNull(facade);
	}
	
	@Test
	public void getAccessRightsTest() {
		facade.createAndAddUser("Bartje", "worker");
		List<AccessRight> AR = facade.getAccessRights();
		assertEquals(2, AR.size());
		assertTrue(AR.contains(AccessRight.ASSEMBLE));
		assertTrue(AR.contains(AccessRight.CHECKLINE));
	}
	
	@Test (expected = NullPointerException.class)
	public void getAccessRightsTestError() {
		facade.getAccessRights();
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void getCarModelSpecificationFromCatalogueTestError() {
		facade.getVehicleSpecificationFromCatalogue("Error");
	}
	
	@Test
	public void loginTestError() {
		try {
			facade.login("test");
		} catch (RoleNotYetAssignedException r) {}
	}
	
	@Test
	public void loginTest() {
		facade.createAndAddUser("test", "worker");
		try {
			facade.login("test");
		} catch (RoleNotYetAssignedException r) {}
	}
	
	@Test
	public void logout() {
		facade.logout();
	}
	
	@Test
	public void getAllCarOptionsInPendingOrdersTest() {
		assertEquals(0, facade.getAllVehicleOptionsInPendingOrders().size());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void processCustomOrderTest() {
		ImmutableClock time = new ImmutableClock(2, 30);
		Vehicle vehicle = new Vehicle(new VehicleSpecification("test",Collections.<VehicleOption> emptySet() , 30));
		facade.processCustomOrder(vehicle, time);
	}
	
	@Test (expected = NullPointerException.class)
	public void processOrdertest() throws IllegalStateException, UnmodifiableException, NotImplementedException {
		facade.processOrder(5);
	}

}
