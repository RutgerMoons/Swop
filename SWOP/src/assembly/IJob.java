package assembly;

import java.util.List;

import order.IOrder;

public interface IJob {

	public IOrder getOrder();
	
	public List<ITask> getTasks();
	
	public boolean isCompleted();
	
	
}
