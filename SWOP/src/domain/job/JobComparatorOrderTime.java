package domain.job;

import java.util.Comparator;
/**
 * This comparator compares 2 Jobs on it's time of order.  
 *
 */
public class JobComparatorOrderTime implements Comparator<IJob> {

	@Override
	public int compare(IJob o1, IJob o2) {
		if(o1==null || o2==null)
			return 0;
		return o1.getOrder().getOrderTime().compareTo(o2.getOrder().getOrderTime());
	}

}
