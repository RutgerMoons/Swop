package domain.carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.car.CustomCarModel;
import domain.car.CustomCarModelCatalogue;

public class CustomCarModelCatalogueTest {

	private CustomCarModelCatalogue catalogue;
	
	@Before
	public void initialize(){
		this.catalogue = new CustomCarModelCatalogue();
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(catalogue.getCatalogue());
	}
	
	@Test
	public void testAddModel(){
		catalogue.addModel("test", new CustomCarModel());
		assertEquals(1, catalogue.getCatalogue().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullModel(){
		catalogue.addModel("abc", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddEmptyDescription(){
		catalogue.addModel("", new CustomCarModel());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullDescription(){
		catalogue.addModel(null, new CustomCarModel());
	}

}
