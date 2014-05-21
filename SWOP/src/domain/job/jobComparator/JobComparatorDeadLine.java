package domain.job.jobComparator;

import java.util.Comparator;

import domain.exception.NotImplementedException;
import domain.job.job.IJob;

/**
 * A class representing a comparator used to compare two IJobs on their deadlines.  
 */
public class JobComparatorDeadLine implements Comparator<IJob> {

	@Override
	public int compare(IJob o1, IJob o2) {
		try {
			return o1.getOrder().getDeadline().compareTo(o2.getOrder().getDeadline());
		} catch (NotImplementedException e) {
			return 0;
		}
	}

}
