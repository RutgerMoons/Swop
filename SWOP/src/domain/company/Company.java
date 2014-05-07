package domain.company;

import java.util.Set;

import domain.clock.Clock;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.order.OrderBook;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.scheduling.WorkloadDivider;
import domain.users.UserBook;
import domain.vehicle.CustomVehicleCatalogue;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;

/**
 * 
 * 
 *
 */
public class Company {

	private UserBook userbook;
	private OrderBook orderbook;
	private PartPicker partpicker;
	private CustomVehicleCatalogue customCatalogue;
	private Logger log;
	private Clock clock;
	private WorkloadDivider workloadDivider;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;

	public Company(){
		this.userbook = new UserBook();
		this.orderbook = new OrderBook();
		int amountOfDetailedHistory = 2;
		this.log = new Logger(amountOfDetailedHistory);
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		assemblyLineObserver.attachLogger(log);
		assemblyLineObserver.attachLogger(orderbook);
		this.clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		this.clock.attachObserver(clockObserver);
		clockObserver.attachLogger(log);
		this.initializeRestrictions();
		this.partpicker = new PartPicker(this.bindingRestrictions, this.optionalRestrictions);
		this.initializeCustom
	}

	private void initializeRestrictions() {
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));

		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));

		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));

	}



}
