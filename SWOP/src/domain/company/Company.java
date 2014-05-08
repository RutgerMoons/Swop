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
import domain.vehicle.IVehicleOption;
import domain.vehicle.VehicleOption;

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

	public Company(Set<BindingRestriction> bindingRestrictions, Set<OptionalRestriction> optionalRestrictions, CustomVehicleCatalogue customCatalogue){
		if (bindingRestrictions == null || optionalRestrictions == null || customCatalogue == null){
			throw new IllegalArgumentException();
		}
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
		this.bindingRestrictions = bindingRestrictions;
		this.optionalRestrictions = optionalRestrictions;
		this.customCatalogue = customCatalogue;
		this.partpicker = new PartPicker(this.bindingRestrictions, this.optionalRestrictions);
		
	}
	
	public void addPartToModel(IVehicleOption part){
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		this.partpicker.getModel().addCarPart(option);
	}

	public void advanceClock(int time){
		this.clock.advanceTime(time);
	}
}
