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
		//kijken of alle workbenches completed zijn, anders moogt ge ni advancen.
		for(WorkBench bench: getWorkbenches())
			if(!bench.isCompleted())
				return;

		Job lastJob = null;
		for(int i=0; i<getWorkbenches().size(); i++){
			WorkBench bench = getWorkbenches().get(i);	
			if(i==0){	//als het de eerste workbench is (de meest linkse op tekeningen, dan moet je een nieuwe job nemen.
				lastJob = bench.getCurrentJob();
				if(bench.getCurrentJob()==null){	//Dit is voor bij de start van een nieuwe werkdag, dan heeft de workbench geen currentjob
					bench.setCurrentJob(getCurrentJobs().get(0));	//de eerste uit de lijst halen dus.
				}else{
					int index = getCurrentJobs().indexOf(bench.getCurrentJob());	
					if((index+1)<getCurrentJobs().size()){		//om indexoutofbounds te voorkomen
						bench.setCurrentJob(getCurrentJobs().get(index+1));
					}else bench.setCurrentJob(null);	//als er geen nieuwe jobs meer zijn, dan moet je zeggen dat de workbench niets te doen heeft
				}
			}else{ //Als het niet de eerste is, moet je de job van de vorige workbench nemen.
				Job prev = bench.getCurrentJob();
				bench.setCurrentJob(lastJob);
				lastJob = prev;
			}
			bench.chooseTasksOutOfJob();	//dan de taken laten selecteren door de workbench
		}
		if(lastJob !=null && lastJob.isCompleted())
			getCurrentJobs().remove(lastJob);	//als de job completed is, dus de auto('s), dan moet je de job natuurlijk removen.
	}

}
