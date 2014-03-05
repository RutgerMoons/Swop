package car;

import java.util.ArrayList;
import java.util.HashMap;

public class CatalogueFiller {
	public CatalogueFiller() {
	}
	
	
	public ArrayList<CarModel> getInitialModels() {
		ArrayList<CarModel> initialModels = new ArrayList<CarModel>();
		initialModels.add(
			new CarModel("Polo",
				new Airco("manual"),
				new Body("sedan"),
				new Color("red"),
				new Engine("standard 2l 4 cilinders"),
				new Gearbox("6 speed manual"),
				new Seat("leather black"),
				new Wheel("comfort")));
		initialModels.add(
				new CarModel("Lada",
					new Airco("automatic climate control"),
					new Body("break"),
					new Color("blue"),
					new Engine("performance 2.5l 6 cilinders"),
					new Gearbox("5 speed automatic"),
					new Seat("leather white"),
					new Wheel("sports (low profile)")));
		return initialModels;
	}
}