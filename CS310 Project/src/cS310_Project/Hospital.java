package cS310_Project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.util.ContextUtils;

public class Hospital {
	private int numBeds = 0;
	private int bedsOccupied = 0;
	private ArrayList<Infected> waitingList = new ArrayList<Infected>();
	private ArrayList<Infected> hospitlisedAgents = new ArrayList<Infected>();
	private int threatenedAgents = 0;
	
	public int getNumbeds() {
		return numBeds;
	}
	
	public int getBedsOccupied() {
		return bedsOccupied;
	}
//	@Watch(watcheeClassName = "cS310_Project.Infected",
//			watcheeFieldNames = "Threatened",
//			query = "colocated",
//			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
//	public void getThreatened() {
//		this.threatenedAgents += 1;
//		Context<Object> context = ContextUtils.getContext(this);
//		List<Susceptible> susAgents = new ArrayList<Susceptible>();
//		Iterable<Object> susceptible = context.getRandomObjects(Susceptible.class, 1);
//	}
	
	
}
