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
	private int numBeds = 350;
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
			watcheeFieldNames = "threatened",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void getThreatened(Infected threatened) {
		if (threatened.isThreatened() == true) {
			this.threatenedAgents += 1;
			this.totalThreatened += 1;
			if (this.numBeds > this.bedsOccupied) {
				threatened.hospitilised();
				this.hospitlisedAgents.add(threatened);
				bedsOccupied += 1;
				Context<Object> context = ContextUtils.getContext(this);
				OccupiedBed newBed = new OccupiedBed();
				context.add(newBed);
				occuBeds.add(newBed);
				
			} else {
				waitingList.add(threatened);
			}
		}
	}
	
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "outHospital",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void freeSpace(Infected threatened) {
		Context<Object> context = ContextUtils.getContext(this);
		this.hospitlisedAgents.remove(threatened);
		this.bedsOccupied -= 1;
		
		OccupiedBed oldBed = occuBeds.get(0);
		context.remove(occuBeds.get(0));
		occuBeds.remove(0);
		this.threatenedAgents -= 1;
		
		if(this.waitingList.size() != 0) {
			this.hospitlisedAgents.add(waitingList.get(0));
			waitingList.get(0).hospitilised();
			waitingList.remove(0);
			this.bedsOccupied += 1;
			
			OccupiedBed newBed = new OccupiedBed();
			context.add(newBed);
			occuBeds.add(newBed);
		}
		
	}
}
