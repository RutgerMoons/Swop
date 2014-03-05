package assembly;

import java.util.ArrayList;
import java.util.List;

import clock.Clock;

public class AssemblyLine {

	private Clock clock;
	private List<WorkBench> workbenches;
	private List<Job> currentJobs;
	private int overtime;
	public AssemblyLine(Clock clock){
		this.clock = clock;
		workbenches = new ArrayList<WorkBench>();
		currentJobs = new ArrayList<Job>();
	}

	public List<WorkBench> getWorkbenches() {
		return workbenches;
	}

	public void setWorkbenches(List<WorkBench> workbenches) {
		if(workbenches==null)
			throw new IllegalArgumentException();
		this.workbenches = workbenches;
	}

	public List<Job> getCurrentJobs() {
		return currentJobs;
	}

	public void setCurrentJobs(List<Job> currentJobs) {
		if(currentJobs==null)
			throw new IllegalArgumentException();
		this.currentJobs = currentJobs;
	}

	public int getOvertime() {
		return overtime;
	}

	public void setOvertime(int overtime) {
		if(overtime<0)
			throw new IllegalArgumentException();
		this.overtime = overtime;
	}

	public Clock getClock() {
		return clock;
	}

	public void addJob(Job job){
		if(job==null)
			throw new IllegalArgumentException();
		else getCurrentJobs().add(job);
	}

	public void addWorkBench(WorkBench bench){
		if(bench==null)
			throw new IllegalArgumentException();
		else getWorkbenches().add(bench);
	}	
	
	public void advance(){
		for(WorkBench bench: getWorkbenches())
			if(!bench.isCompleted())
				return;
		
		Job lastJob = null;
		for(int i=0; i<getWorkbenches().size(); i++){
			WorkBench bench = getWorkbenches().get(i);
			if(i==0){
				lastJob = bench.getCurrentJob();
				if(bench.getCurrentJob()==null){
					bench.setCurrentJob(getCurrentJobs().get(0));
				}else{
					int index = getCurrentJobs().indexOf(bench.getCurrentJob());
					if((index+1)<getCurrentJobs().size()){
						bench.setCurrentJob(getCurrentJobs().get(index+1));
					}
				}
			}else{
				Job prev = bench.getCurrentJob();
				if(lastJob!=null)
					bench.setCurrentJob(lastJob);
				lastJob = prev;
			}
			bench.chooseTasksOutOfJob();
		}
		if(lastJob !=null && lastJob.isCompleted())
			getCurrentJobs().remove(lastJob);
	}

}
