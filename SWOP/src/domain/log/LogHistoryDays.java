package domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogHistoryDays extends LogHistory {
	
	private int carsProducedToday;
	private ArrayList<Integer> history;
	private ArrayList<Integer> completeHistory;
	
	public LogHistoryDays(int numberOfDays) {
		super(numberOfDays);
		history = new ArrayList<Integer>();
		completeHistory = new ArrayList<Integer>();
	}

	@Override
	public void shift() {
		history.add(carsProducedToday);
		this.carsProducedToday = 0;
		if (history.size() > numberOfDays) {
			completeHistory.add(history.remove(0));
		}
	}
	
	public void incrementAmountOfCarsProducedToday() {
		carsProducedToday++;
	}

	@Override
	public List<Integer> getCompleteHistory() {
		ArrayList<Integer> complete = new ArrayList<>();
		for (Integer i : history) {
			complete.add(i);
		}
		for (Integer i : completeHistory) {
			complete.add(i);
		}
		Collections.sort(complete);
		return Collections.unmodifiableList(complete);
	}
	
	public List<Integer> getHistory() {
		return Collections.unmodifiableList(history);
	}

}
