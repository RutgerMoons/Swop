package domain.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import domain.car.CarPart;
import domain.job.Job;

public class Scheduler {

	private SchedulingAlgorithm schedulingAlgorithm;
	private final int amountOfWorkBenches;
	
	public Scheduler(int amountOfWorkBenches) {
		this.amountOfWorkBenches = amountOfWorkBenches;
	}
	
	public void addCustomJob(Job customJob) {
		this.schedulingAlgorithm.AddCustomJob(customJob);
	}
	
	public void addStandardJob(Job standardJob) {
		this.schedulingAlgorithm.AddStandardJob(standardJob);
	}
	
	public void switchToFifo() {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches); 
		}
		else {
			PriorityQueue<Job> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<Job> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			this.schedulingAlgorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);
			this.schedulingAlgorithm.transform(customJobs, standardJobs);
		}
	}
	
	public void switchToBatch(List<CarPart> carParts) {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = new SchedulingAlgorithmBatch(carParts, amountOfWorkBenches); 
		}
		else {
			PriorityQueue<Job> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<Job> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			this.schedulingAlgorithm = new SchedulingAlgorithmBatch(carParts, amountOfWorkBenches);
			this.schedulingAlgorithm.transform(customJobs, standardJobs);
		}
	}
	
}
