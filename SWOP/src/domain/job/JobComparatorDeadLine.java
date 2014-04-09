package domain.job;

import java.util.Comparator;

import domain.exception.NotImplementedException;

public class JobComparatorDeadLine implements Comparator<IJob> {

	
	//TODO: elegantere oplossing?
	@Override
	public int compare(IJob o1, IJob o2) {
		try {
			return o1.getOrder().getDeadline().compareTo(o2.getOrder().getDeadline());
		} catch (NotImplementedException e) {
			return 0;
		}
	}

}