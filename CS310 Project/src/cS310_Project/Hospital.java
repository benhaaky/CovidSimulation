package cS310_Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.PropertyEquals;
import repast.simphony.query.Query;
import repast.simphony.util.ContextUtils;

public class Hospital {
	private int numBeds;
	private int bedsOccupied = 0;
	private ArrayList<Infected> waitingList = new ArrayList<Infected>();
	private ArrayList<Infected> hospitlisedAgents = new ArrayList<Infected>();
	private ArrayList<OccupiedBed> occuBeds = new ArrayList<OccupiedBed>();
	private int threatenedAgents = 0;
	private int totalThreatened = 0;
	
	public Hospital() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		Random r = new Random();
		//Get user input for recovery time
		numBeds = params.getInteger("hospitalBeds");
	}
	
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
				OccupiedBed newBed = new OccupiedBed(threatened);
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
		System.out.println("OUTOUTO");
		Context<Object> context = ContextUtils.getContext(this);
		this.hospitlisedAgents.remove(threatened);
		this.bedsOccupied -= 1;
		
		OccupiedBed oldBed = occuBeds.get(0);
		oldBed = null;
		context.remove(occuBeds.get(0));
		occuBeds.remove(0);
		this.threatenedAgents -= 1;
		
		if(this.waitingList.size() != 0) {
			this.hospitlisedAgents.add(waitingList.get(0));
			waitingList.get(0).hospitilised();
			OccupiedBed newBed = new OccupiedBed(waitingList.get(0));
			context.add(newBed);
			occuBeds.add(newBed);
			waitingList.remove(0);
			this.bedsOccupied += 1;
			

		}
		
	}
	@Watch(watcheeClassName = "cS310_Project.Infected",
			watcheeFieldNames = "extinct",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void extinctAgent(Infected extinct) {

		
		if(this.waitingList.size() != 0) {

			waitingList.remove(extinct);
			

		}
		
	}
}
