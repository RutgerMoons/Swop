package domain.carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import domain.car.CarModelCatalogue;
import domain.car.CarModelCatalogueFiller;
import domain.car.CarModelSpecification;
import domain.exception.AlreadyInMapException;

public class CatalogueTest {

	
	@Test
	public void Test() {

		CarModelCatalogue cat = new CarModelCatalogue();
		CarModelCatalogueFiller filler = new CarModelCatalogueFiller();
		
		cat.initializeCatalogue(filler.getInitialModels());
		assertNotNull(cat.getCatalogue());
		assertEquals(3, cat.getCatalogue().entrySet().size());

	}

	@Test(expected = IllegalArgumentException.class)
	public void Test2() {
		CarModelCatalogue cat = new CarModelCatalogue();
		Set<CarModelSpecification> list = new HashSet<CarModelSpecification>();
		list.add(null);
		cat.initializeCatalogue(list);

	}

	@Test(expected = IllegalArgumentException.class)
	public void Test3() throws AlreadyInMapException {
		CarModelCatalogue cat = new CarModelCatalogue();
		cat.addModel(null);
	}
}
