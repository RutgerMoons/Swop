package domain.job;

import java.util.Comparator;

import domain.exception.NotImplementedException;

public class JobComparatorDeadLine implements Comparator<Job> {

	
	//TODO: elegantere oplossing?
	@Override
	public int compare(Job o1, Job o2) {
		try {
			return o1.getOrder().getDeadline().compareTo(o2.getOrder().getDeadline());
		} catch (NotImplementedException e) {
			return 0;
		}
	}

}
