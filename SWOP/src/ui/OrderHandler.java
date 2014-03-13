package ui;

import java.util.ArrayList;
import java.util.HashMap;

import car.Airco;
import car.Body;
import car.CarModel;
import car.CarModelCatalogue;
import car.CarPart;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import order.Order;
import order.OrderBook;
import users.GarageHolder;
import users.User;

/**
 * 
 * Defines the program flow for the 'Order New Car' use case.
 *
 */
public class OrderHandler extends UseCaseHandler{

	private OrderBook orderBook;
	private UIFacade UIFacade;
	private CarModelCatalogue catalogue;

	/**
	 * Construct a new OrderHandler
	 * @param UIFacade
	 * 			The UIfacade this OrderHandler has to use to communicate with the user.
	 * @param orderBook
	 * 			The OrderBook this OrderHandler has to access if it needs an OrderBook.
	 * @param catalogue
	 * 			The CarModelCatalogue from which this OrderHandler can get its possible CarModels.
	 */
	public OrderHandler (UIFacade UIFacade, OrderBook orderBook, CarModelCatalogue catalogue){
		this.UIFacade = UIFacade;
		this.orderBook = orderBook;
		this.catalogue = catalogue;
	}

	/**
	 * Indicates if the user is authorized to be part of the use case.
	 * @param user
	 * 			The user of which we want to get to know if he's authorized.
	 * @return
	 * 			A boolean indicating if the user is authorized.
	 */
	@Override
	public boolean mayUseThisHandler(User user){
		return user instanceof GarageHolder;
	}

	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 */
	@Override
	public void executeUseCase(User user) throws IllegalArgumentException{
		if (user == null) {
			throw new IllegalArgumentException();
		}
		if(mayUseThisHandler(user)){
			showOrders(user);
			Order order = placeNewOrder(user);

			// order == null if order isn't confirmed by user
			if(order != null) {
				showNewOrder(order);
			}
		}
	}

	/**
	 * Shows the user's current pending (with estimated time of completion) and completed orders.
	 * @param user
	 */
	public void showOrders(User user){
		ArrayList<Order> pendingOrders;
		ArrayList<Order> completedOrders;
		ArrayList<String> pendingOrdersStrings = new ArrayList<String>();
		ArrayList<String> completedOrdersStrings = new ArrayList<String>();

		if(this.orderBook.getPendingOrders().containsKey(user.getName()) &&
				!this.orderBook.getPendingOrders().get(user.getName()).isEmpty()) {
			pendingOrders = this.orderBook.getPendingOrders().get(user.getName());
			for(Order order: pendingOrders){
				pendingOrdersStrings.add(order.toString());
			}
		}
		else {
			pendingOrdersStrings = null;
		}

		if(this.orderBook.getCompletedOrders().containsKey(user.getName())) {
			completedOrders = this.orderBook.getCompletedOrders().get(user.getName());
			for(Order order: completedOrders){
				completedOrdersStrings.add(order.toString());
			}
		}	
		else {
			completedOrdersStrings = null;
		}

		this.UIFacade.showPendingOrders(pendingOrdersStrings);
		this.UIFacade.showCompletedOrders(completedOrdersStrings);

	}

	/**
	 * Creates a new order. 
	 * @param user
	 * @return
	 * 			Returns null if the user cancels his order somewhere in this process.
	 * 			Otherwise, it returns the order the user has just placed.
	 */
	public Order placeNewOrder(User user){
		boolean wantToOrder = this.UIFacade.askContinue();
		if(!wantToOrder) {
			return null;
		}
		else{
			String model = UIFacade.chooseModel(catalogue.getCatalogue().keySet());

			//TODO wat doet dit?
			//--> 1e lijn weg
			//--> 2e lijn: CarModel carModel = catalogue.getCatalogue().get(model);
			// ===>hashmap in catalogue moet objecten van het type carModel bevatten, 
			//       zo wordt ervoor gezorgd dat elk carModel maar 1 keer aangemaakt wordt, en in elk order van dat model naar dat ene object verwezen wordt
			HashMap<Class<?>, CarPart> parts = catalogue.getCatalogue().get(model);
			CarModel carModel = new CarModel(model, (Airco)parts.get(Airco.class), (Body)parts.get(Body.class),
					(Color) parts.get(Color.class), (Engine)parts.get(Engine.class), 
					(Gearbox)parts.get(Gearbox.class), (Seat)parts.get(Seat.class), 
					(Wheel)parts.get(Wheel.class));

			int quantity = UIFacade.getQuantity();
			int[] estimatedTime = new int[1];
			estimatedTime[0] = -1;
			UIFacade.showOrder(quantity, carModel.toString(), estimatedTime);

			if(!this.UIFacade.askContinue()){
				executeUseCase(user);
				return null;
			}
			else{
				Order order = new Order(user.getName(), carModel, quantity);
				orderBook.addOrder(order);
				return order;
			}
		}
	}

	/**
	 * Shows the new order the user has just placed (with estimated completion time)
	 * @param user
	 * @param order
	 */
	public void showNewOrder(Order order){
		UIFacade.showOrder(order.getQuantity(), order.getDescription().toString(), order.getEstimatedTime());
	}
}
