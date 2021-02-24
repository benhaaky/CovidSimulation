package cS310_Project;

import java.util.Random;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.parameter.Parameters;

public class RnumberCalc {
	private double infectionTick = 0.0;
	private double currentInfections = 0.0;
	private double totalInfections = 0.0;
	private double symptomatic = 0.0;
	
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "agentsInfected",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void calculateR() {
		this.infectionTick += 1;
		currentInfections += 1;
		
	}
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "recovered",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void agentRecover() {
		this.currentInfections -= 1;
		
	}
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "symptomatic",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void symptomWatch() {
		this.symptomatic += 1;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 6)
	public void step() {
		System.out.println("This tick: " + this.infectionTick);
		System.out.println("Total: " + this.currentInfections);
		double tR = 0;
		double Rt = 0;
		if (this.currentInfections != 0 && this.infectionTick != 0) {
			tR = this.currentInfections/this.infectionTick;
			Rt = this.infectionTick/this.currentInfections;
		}
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		double recoveryTicks = params.getInteger("infectionTime");
		double Rnum = Rt*recoveryTicks/2;
		System.out.println("total/R: " + tR);
		System.out.println("R/total: " + Rt);
		System.out.println("R: " + Rnum);
		this.infectionTick = 0;
		
	}
}
