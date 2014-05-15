package domain.order.orderVisitor;

import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;

public interface OrderVisitor {

	public void visit(CustomOrder order);
	
	public void visit(StandardOrder order);
	
}
