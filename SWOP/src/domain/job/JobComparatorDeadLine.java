package domain.job;

import java.util.Comparator;

public class JobComparatorDeadLine implements Comparator<Job> {

	@Override
	public int compare(Job o1, Job o2) {
		return o1.getOrder().getOrderTime().compareTo(o2.getOrder().getOrderTime());
	}

}
