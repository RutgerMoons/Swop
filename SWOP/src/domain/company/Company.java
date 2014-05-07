package domain.company;

import domain.clock.Clock;
import domain.log.Logger;
import domain.order.OrderBook;
import domain.restriction.PartPicker;
import domain.scheduling.WorkloadDivider;
import domain.users.UserBook;
import domain.vehicle.CustomVehicleCatalogue;

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
}
