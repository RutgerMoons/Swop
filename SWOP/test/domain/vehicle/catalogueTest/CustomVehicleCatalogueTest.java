package domain.vehicle.catalogueTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.vehicle.catalogue.CustomVehicleCatalogue;
import domain.vehicle.vehicle.CustomVehicle;

public class CustomVehicleCatalogueTest {

	private CustomVehicleCatalogue catalogue;
	
	@Before
	public void initialize(){
		this.catalogue = new CustomVehicleCatalogue();
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(catalogue.getCatalogue());
	}
	
	@Test
	public void testAddModel(){
		catalogue.addModel("test", new CustomVehicle());
		assertEquals(1, catalogue.getCatalogue().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullModel(){
		catalogue.addModel("abc", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddEmptyDescription(){
		catalogue.addModel("", new CustomVehicle());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullDescription(){
		catalogue.addModel(null, new CustomVehicle());
	}

}
