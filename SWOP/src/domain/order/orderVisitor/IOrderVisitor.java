package domain.order.orderVisitor;

import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.order.order.UnmodifiableOrder;

public interface IOrderVisitor {

	public void visit(CustomOrder order);
	
	public void visit(StandardOrder order);
	
	public void visit(UnmodifiableOrder order);
}
