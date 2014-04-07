package domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.order.Delay;

public class LogHistoryDelays extends LogHistory {

	private Delay latestDelay;
	private ArrayList<Delay> history;
	private ArrayList<Integer> completeHistory;
	
	public LogHistoryDelays(int numberOfDays) {
		super(numberOfDays);
		history = new ArrayList<Delay>();
		completeHistory = new ArrayList<Integer>();
	}
	
	@Override
	public void shift() {
		history.add(latestDelay);
		if (history.size() > numberOfDays) {
			completeHistory.add(history.remove(0).getDelay());
		}
	}

	@Override
	public List<Integer> getCompleteHistory() {
		ArrayList<Integer> complete = new ArrayList<>();
		for (Delay d : history) {
			complete.add(d.getDelay());
		}
		for (Integer i : completeHistory) {
			complete.add(i);
		}
		return Collections.unmodifiableList(complete);
	}
	
	public void addNewDelay(Delay newDelay) {
		if (newDelay == null) {
			throw new IllegalArgumentException();
		}
		if (newDelay.getDelay() != 0) {
			latestDelay = newDelay;
			shift();
		}
	}
	
	public List<Delay> getHistory() {
		return Collections.unmodifiableList(history);
	}

}
