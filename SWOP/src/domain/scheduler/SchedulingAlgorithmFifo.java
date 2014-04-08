package domain.scheduler;

import java.util.ArrayList;
import java.util.PriorityQueue;

import domain.assembly.Job;

public class SchedulingAlgorithmFifo extends SchedulingAlgorithm {
	
	private PriorityQueue<Job> customJobs;
	private PriorityQueue<Job> standardJobs;
	
	public SchedulingAlgorithmFifo(){
		customJobs = new PriorityQueue<Job>();
		standardJobs = new PriorityQueue<Job>();
	}

	@Override
	public void transform(PriorityQueue<Job> customJobs, ArrayList<Job> standardJobs) {
		if(customJobs == null || standardJobs == null){
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		this.standardJobs.addAll(standardJobs);
	}

	@Override
	public Job retrieveNext(int minutesTillEndOfDay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PriorityQueue<Job> getCustomJobs() {
		return this.customJobs.;
	}

	@Override
	public ArrayList<Job> getStandardJobs() {
		// TODO Auto-generated method stub
		return null;
	}

}
