package cS310_Project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.PropertyEquals;
import repast.simphony.query.Query;
import repast.simphony.util.ContextUtils;

public class Hospital {
	private int numBeds = 0;
	private int bedsOccupied = 0;
	private ArrayList<Infected> waitingList = new ArrayList<Infected>();
	private ArrayList<Infected> hospitlisedAgents = new ArrayList<Infected>();
	private ArrayList<OccupiedBed> occuBeds = new ArrayList<OccupiedBed>();
	private int threatenedAgents = 0;
	private int totalThreatened = 0;
	
	public int getNumbeds() {
		return numBeds;
	}
	
	public int getBedsOccupied() {
		return bedsOccupied;
	}
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "Threatened",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void getThreatened(Infected threatened) {
		this.threatenedAgents += 1;
		this.totalThreatened += 1;
		if (this.numBeds > this.bedsOccupied) {
			threatened.hospitilised();
			this.hospitlisedAgents.add(threatened);
			bedsOccupied += 1;
			
			
		} else {
			waitingList.add(threatened);
		}
		
	}
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "outHospital",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void freeSpace(Infected threatened) {
		this.threatenedAgents -= 1;
		this.hospitlisedAgents.remove(threatened);
		this.bedsOccupied -= 1;
		if(this.waitingList.size() != 0) {
			this.hospitlisedAgents.add(waitingList.get(0));
			waitingList.remove(0);
			this.bedsOccupied += 1;
		}
		
	}
	
	
	
	
}
