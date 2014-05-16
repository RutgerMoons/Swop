package domain.job.jobComparator;

import java.util.Comparator;

import domain.job.job.IJob;
/**
 * A class representing an comparator used to compare two IJobs on their time of ordering.  
 */
public class JobComparatorOrderTime implements Comparator<IJob> {

	@Override
	public int compare(IJob o1, IJob o2) {
		if(o1==null || o2==null)
			return 0;
		return o1.getOrder().getOrderTime().compareTo(o2.getOrder().getOrderTime());
	}

}
