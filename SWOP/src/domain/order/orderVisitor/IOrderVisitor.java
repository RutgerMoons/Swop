package domain.order.orderVisitor;

import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.order.order.UnmodifiableOrder;

/**
 * An interface used to represent an OrderVisitor. 
 * It consists of methods what to do with each type of order. 
 *
 */
public interface IOrderVisitor {

	/**
	 * Visit a CustomOrder.
	 */
	public void visit(CustomOrder order);
	
	/**
	 * Visit a StandardOrder.
	 */
	public void visit(StandardOrder order);
	
	/**
	 * Visit an UnmodifiableOrder.
	 */
	public void visit(UnmodifiableOrder order);
}
