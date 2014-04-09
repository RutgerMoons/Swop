package domain.job;

import java.util.Comparator;

public class JobComparatorOrderTime implements Comparator<IJob> {

	@Override
	public int compare(IJob o1, IJob o2) {
		return o1.getOrder().getOrderTime().compareTo(o2.getOrder().getOrderTime());
	}

}
