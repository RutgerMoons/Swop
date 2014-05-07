package domain.facadeTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.facade.Facade;
import domain.users.AccessRight;

public class FacadeTest {

	private Facade facade;
	
	@Before
	public void initialize() {
		facade = new Facade(Collections.EMPTY_SET, Collections.EMPTY_SET);
	}
	
	@Test
	public void testAdvanceTime() {
		assertNotNull(facade);
		facade.advanceClock(360);
		assertNotNull(facade);
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
	
	@Test
	public void getBlockingWorkBenchesTest() {
		assertEquals(0, facade.getBlockingWorkBenches().size());
		assertTrue(facade.canAssemblyLineAdvance());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getCarModelSpecificationFromCatalogueTestError() {
		facade.getCarModelSpecificationFromCatalogue("Error");
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
		assertEquals(0, facade.getAllCarOptionsInPendingOrders().size());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void processCustomOrderTest() {
		facade.processCustomOrder("test", "5");
	}
	
	@Test (expected = NullPointerException.class)
	public void processOrdertest() throws IllegalStateException, UnmodifiableException, NotImplementedException {
		facade.processOrder(5);
	}

}
