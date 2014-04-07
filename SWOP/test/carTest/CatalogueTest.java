package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelCatalogue;
import domain.car.CarModelCatalogueFiller;
import domain.car.CarPart;
import domain.car.CarPartCatalogue;
import domain.car.CarPartCatalogueFiller;
import domain.car.CarPartType;
import domain.exception.AlreadyInMapException;

public class CatalogueTest {



	@Test
	public void Test() {

		CarPartCatalogue catalogueCarPart = new CarPartCatalogue();
		CarPartCatalogueFiller partFiller = new CarPartCatalogueFiller(catalogueCarPart);
		partFiller.initializeCarParts();
		CarModelCatalogue cat = new CarModelCatalogue(catalogueCarPart);
		CarModelCatalogueFiller filler = new CarModelCatalogueFiller();
		
		cat.initializeCatalogue(filler.getInitialModels());
		assertNotNull(cat.getCatalogue());
		assertEquals(2, cat.getCatalogue().entrySet().size());

	}

	@Test(expected = IllegalArgumentException.class)
	public void Test2() {
		CarModelCatalogue cat = new CarModelCatalogue(new CarPartCatalogue());
		Set<CarModel> list = new HashSet<CarModel>();
		list.add(null);
		cat.initializeCatalogue(list);

	}

	@Test(expected = IllegalArgumentException.class)
	public void Test3() throws AlreadyInMapException {
		CarModelCatalogue cat = new CarModelCatalogue(new CarPartCatalogue());
		CarModel car = new CarModel("test");
		car.addCarPart(new CarPart("bla", true, CarPartType.AIRCO));
		cat.addModel(car);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Test4() {
		CarModelCatalogue cat = new CarModelCatalogue(new CarPartCatalogue());
		cat.addModel(null);
	}
}
