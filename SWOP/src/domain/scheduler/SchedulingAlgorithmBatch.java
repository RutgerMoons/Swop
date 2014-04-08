package domain.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import domain.car.CarPart;
import domain.clock.UnmodifiableClock;
import domain.job.Job;
import domain.job.JobComparatorDeadLine;
import domain.job.JobComparatorOrderTime;

public class SchedulingAlgorithmBatch extends SchedulingAlgorithm {

	private PriorityQueue<Job> customJobs;
	private PriorityQueue<Job> standardJobs;
	private PriorityQueue<Job> batchJobs;
	private List<CarPart> carParts;
	private final int amountOfWorkBenches;
	
	public SchedulingAlgorithmBatch(List<CarPart> carParts, int amountOfWorkBenches) {
		if (carParts == null) {
			throw new IllegalArgumentException();
		}
		this.carParts = carParts;
		this.amountOfWorkBenches = amountOfWorkBenches;
		customJobs = new PriorityQueue<>(0, new JobComparatorDeadLine());
		standardJobs = new PriorityQueue<Job>(0, new JobComparatorOrderTime());
		batchJobs = new PriorityQueue<Job>(0, new JobComparatorOrderTime());
	}
	
	@Override
	public void transform(PriorityQueue<Job> customJobs, ArrayList<Job> jobs) {
		if(customJobs == null || jobs == null ){
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		
		//split jobs into the two remaining queues based on carParts
		for(Job job : jobs){
			if(job.getOrder().getDescription().getCarParts().values().containsAll(this.carParts)){
				this.batchJobs.add(job);
			}
			else{
				this.standardJobs.add(job);
			}
		}
	}

	@Override
	public Job retrieveNext(int currentTotalProductionTime, int minutesTillEndOfDay, UnmodifiableClock currentTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PriorityQueue<Job> getCustomJobs() {
		PriorityQueue<Job> pq = new PriorityQueue<>(customJobs.size(), new JobComparatorDeadLine());
		pq.addAll(this.customJobs);
		return pq;
	}

	@Override
	public ArrayList<Job> getStandardJobs() {
		ArrayList<Job> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		list.addAll(this.batchJobs);
		return list;
	}

	@Override
	public void AddCustomJob(Job customJob) {
		if (customJob == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs.add(customJob);
	}

	@Override
	public void AddStandardJob(Job standardJob) {
		if (standardJob == null) {
			throw new IllegalArgumentException();
		}
		if(standardJob.getOrder().getDescription().getCarParts().values().containsAll(this.carParts)){
			this.batchJobs.add(standardJob);
		}
		else{
			this.standardJobs.add(standardJob);
		}
	}

}
