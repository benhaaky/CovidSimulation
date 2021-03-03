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
	//Ticks to recovery
	private double recoveryTime;
	//Tick infected
	private double timeOfInfection;
	//Store if the agents condition has worsened
	private boolean threatened = false;
	//Has agent been hospitilised
	private boolean inHospital = false;
	
	private boolean outHospital = false;
	
	//Probably not needed
	private boolean diagnosed;
	//Is agent symptomatic
	private boolean symptomatic = false;
	//How long until agent is symptomatic
	private double timeToSymptoms;
	private double timeToWorsen;
	//Was the agent previously vaccinated
	private boolean vaccinated;
	//Amount of other agent this has infected
	private int agentsInfected = 0;
	private boolean recovered = false;
	
	public Infected(ContinuousSpace<Object> space, Grid<Object> grid, boolean vaccine, boolean vulnerable) {
		super(space, grid, vulnerable);
		// Set recovery time from user input
		this.vaccinated = vaccine;
		this.setVulnerable(vulnerable);
		//Calculate how long the agent is infected for
		timeOfInfection = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		Parameters params = RunEnvironment.getInstance().getParameters();
		Random r = new Random();
		//Get user input for recovery time
		double recoveryTicks = params.getInteger("infectionTime");
		//Randomly distribute the time
		double recoverySTD = recoveryTicks*0.3;
		this.recoveryTime = r.nextGaussian()*recoverySTD+recoveryTicks;
		//Probability agent is symptomatic
		double random = Math.random();
		double rand1 = Math.random();
		double rand2 = Math.random();
		if (this.atRisk() || random < 0.2) {
			timeToSymptoms = recoveryTime/(2.5+rand1);
			timeToWorsen = timeToSymptoms*(1.3+rand2);
		} else {
			timeToSymptoms = recoveryTime+10;
		}
	}
	public boolean isThreatened() {
		return this.threatened;
	}
	public void hospitilised() {
		this.inHospital = true;
	}
	
	//Check if an infected agent has recovered
	public void recover() {
		
		
		
		// get current time
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		// Check if enough time has passed
		if (currentTime > (recoveryTime+timeOfInfection)) {
			if (this.inHospital == true) {
				this.outHospital = true;
			}
			this.recovered = true;
			//Replace agent with recovered agent
			GridPoint GridPoint = getGrid().getLocation(this);
			NdPoint spacePoint = getSpace().getLocation(this);
			Context<Object> context = ContextUtils.getContext(this);
			//update recovered agents location
			Recovered newRecovered = new Recovered(this.getSpace(), this.getGrid(), this, this.atRisk());
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
		
		List<Susceptible> susnts = new ArrayList<Susceptible>();
		List<Object> susAgents = new ArrayList<Object>();
		List<Vaccinated> vacAgents = new ArrayList<Vaccinated>();
		//Get objects in agents grid location
		Object object = getGrid().getObjectsAt(GridPoint.getX(), GridPoint.getY());
		//Loop through agents in same grid space
		for (Object obj : getGrid().getObjectsAt(GridPoint.getX(), GridPoint.getY())) {
			// Check if the agent can be infected
 			if (obj instanceof Susceptible) {
				susAgents.add(obj);
					
			} else if (obj instanceof Vaccinated) {
				if (!((Vaccinated) obj).isImmune()) {
					susAgents.add(obj);

				}
				
			}
			
		}
		// Loop through agents to be infected
		for (Object sus : susAgents) {
			// Replace susceptible agent with infected agent
			NdPoint spacePoint = getSpace().getLocation(sus);
			Context<Object> context = ContextUtils.getContext(this);
			Infected newInfected;
			if (sus instanceof Susceptible) {
				newInfected = new Infected(getSpace(), getGrid(), false, ((Agent) sus).atRisk());
				newInfected.setDestination(((Susceptible) sus).getDestination());
			} else {
				newInfected = new Infected(getSpace(), getGrid(), true, ((Agent) sus).atRisk());
				newInfected.setDestination(((Vaccinated) sus).getDestination());
			}
			
			context.add(newInfected);
			getSpace().moveTo(newInfected, spacePoint.getX(), spacePoint.getY());
			getGrid().moveTo(newInfected, GridPoint.getX(), GridPoint.getY());
			

			
			context.remove(sus);
			sus = null;
			this.agentsInfected += 1;
		}
	}
	
	public void startSymptoms() {
		displaySymptoms();
		double random = Math.random();
		
	}
	public void startWorsen() {
		if(this.atRisk()) {
			
			this.threatened = true;
			this.recoveryTime = recoveryTime*1.25;
		}
	}
	public void extinct() {
		// get current time
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		// Check if enough time has passed
		if (currentTime > (recoveryTime+timeOfInfection)) {
			
			Context<Object> context = ContextUtils.getContext(this);
			Extinct newExtinct = new Extinct();
			context.add(newExtinct);
			context.remove(this);
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
		super.step();
		//Get grid location
		infect();
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		// Check if enough time has passed
		if (currentTime > (timeToSymptoms+timeOfInfection) && this.symptomatic == false) {
			
			startSymptoms();
		}
		if (currentTime > (timeToWorsen+timeOfInfection) && this.threatened == false) {
			
			startWorsen();
		}

		if(this.threatened == true && this.inHospital == false) {
			
			extinct();
		} else if (this.threatened == false || this.inHospital == true) {
			
			recover();
		}
		
	}
	

}
