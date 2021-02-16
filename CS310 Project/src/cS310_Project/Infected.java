package cS310_Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Infected extends Agent {
	private double recoveryTime;
	private double infectionTime;
	
	private boolean diagnosed;
	private boolean symptomatic;
	
	private int agentsInfected = 0;
	
	public Infected(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
		// Set recovery time from user input
		infectionTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		Parameters params = RunEnvironment.getInstance().getParameters();
		Random r = new Random();
		
		double recoveryTicks = params.getInteger("infectionTime");
		double recoverySTD = recoveryTicks*0.1;
		this.recoveryTime = r.nextGaussian()*recoverySTD+recoveryTicks;
	}
	//Check if an infected agent has recovered
	public void recover() {
		// get current time
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		// Check if enough time has passed
		if (currentTime > (recoveryTime+infectionTime)) {
			//Replace agent with recovered agent
			GridPoint GridPoint = getGrid().getLocation(this);
			NdPoint spacePoint = getSpace().getLocation(this);
			Context<Object> context = ContextUtils.getContext(this);
			
			Recovered newRecovered = new Recovered(this.getSpace(), this.getGrid(), this);
			newRecovered.setDestination(this.getDestination());
			context.add(newRecovered);
			getSpace().moveTo(newRecovered, spacePoint.getX(), spacePoint.getY());
			getGrid().moveTo(newRecovered, GridPoint.getX(), GridPoint.getY());
			context.remove(this);
		}
	}
	
	// Infect other agents
	public void infect() {
		
		GridPoint GridPoint = getGrid().getLocation(this);
		
		List<Susceptible> susAgents = new ArrayList<Susceptible>();
		//Get objects in agents grid location
		Object object = getGrid().getObjectsAt(GridPoint.getX(), GridPoint.getY());
		//Loop through agents in same grid space
		for (Object obj : getGrid().getObjectsAt(GridPoint.getX(), GridPoint.getY())) {
			// Check if the agent can be infected
 			if (obj instanceof Susceptible) {
				susAgents.add((Susceptible) obj);
					
			}
			
		}
		// Loop through agents to be infected
		for (Susceptible sus : susAgents) {
			// Replace susceptible agent with infected agent
			NdPoint spacePoint = getSpace().getLocation(sus);
			Context<Object> context = ContextUtils.getContext(this);
			
			Infected newInfected = new Infected(getSpace(), getGrid());
			newInfected.setDestination(sus.getDestination());
			context.add(newInfected);
			getSpace().moveTo(newInfected, spacePoint.getX(), spacePoint.getY());
			getGrid().moveTo(newInfected, GridPoint.getX(), GridPoint.getY());
			

			
			context.remove(sus);
			sus = null;
			this.agentsInfected += 1;
		}
	}
	
	public void displaySymptoms() {
		this.symptomatic = true;
	}
	
	public void detectInfection() {
		this.diagnosed = true;
	}
	
	@ScheduledMethod(start = 1, interval = 1, shuffle=true, priority=5)
	public void step() {
		//Get grid location
		infect();
		super.step();
		recover();
		
	}

}
