package domain.vehicle.catalogueTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import view.VehicleSpecificationCatalogueFiller;
import domain.exception.AlreadyInMapException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;

public class VehicleSpecificationCatalogueTest {

	
	@Test
	public void Test() {

		VehicleSpecificationCatalogue cat = new VehicleSpecificationCatalogue();
		VehicleSpecificationCatalogueFiller filler = new VehicleSpecificationCatalogueFiller();
		
		cat.initializeCatalogue(filler.getInitialModels());
		assertNotNull(cat.getCatalogue());
		assertEquals(5, cat.getCatalogue().entrySet().size());

	}

	@Test(expected = IllegalArgumentException.class)
	public void Test2() {
		VehicleSpecificationCatalogue cat = new VehicleSpecificationCatalogue();
		Set<VehicleSpecification> list = new HashSet<VehicleSpecification>();
		list.add(null);
		cat.initializeCatalogue(list);

	}

	@Test(expected = IllegalArgumentException.class)
	public void Test3() throws AlreadyInMapException {
		VehicleSpecificationCatalogue cat = new VehicleSpecificationCatalogue();
		cat.addModel(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestIllegalInitialisation(){
		VehicleSpecificationCatalogue cat = new VehicleSpecificationCatalogue();
		cat.initializeCatalogue(null);
	}
}
