package cS310_Project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Infected extends Agent {
	private double recoveryTime = 500000;
	private double infectionTime;
	
	public Infected(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
		infectionTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		System.out.println(infectionTime);
		// TODO Auto-generated constructor stub
	}
	public void recover() {
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (currentTime > (recoveryTime+infectionTime)) {
			System.out.println("true");
			GridPoint GridPoint = getGrid().getLocation(this);
			NdPoint spacePoint = getSpace().getLocation(this);
			Context<Object> context = ContextUtils.getContext(this);
			
			Recovered newRecovered = new Recovered(this.getSpace(), this.getGrid(), this);
			newRecovered.setDestination(this.getDestination());
			context.add(newRecovered);
			getSpace().moveTo(newRecovered, spacePoint.getX(), spacePoint.getY());
			getGrid().moveTo(newRecovered, GridPoint.getX(), GridPoint.getY());
			context.remove(this);
			System.out.println("Recovered");
		}
	}
	
	public void infect() {
		GridPoint GridPoint = getGrid().getLocation(this);
		
		List<Susceptible> susAgents = new ArrayList<Susceptible>();
		Object object = getGrid().getObjectsAt(GridPoint.getX(), GridPoint.getY());
		for (Object obj : getGrid().getObjectsAt(GridPoint.getX(), GridPoint.getY())) {
 			if (obj instanceof Susceptible) {
				susAgents.add((Susceptible) obj);
					
			}
			
		}
		for (Susceptible sus : susAgents) {
			NdPoint spacePoint = getSpace().getLocation(sus);
			Context<Object> context = ContextUtils.getContext(this);
			
			Infected newInfected = new Infected(getSpace(), getGrid());
			newInfected.setDestination(sus.getDestination());
			context.add(newInfected);
			getSpace().moveTo(newInfected, spacePoint.getX(), spacePoint.getY());
			getGrid().moveTo(newInfected, GridPoint.getX(), GridPoint.getY());
			

			
			System.out.println("Infected");
			context.remove(sus);
			sus = null;
			System.out.println("Removed");
		}
	}
	@ScheduledMethod(start = 1, interval = 1000, shuffle=true)
	public void step() {
		//Get grid location
		System.out.println("infect");
		infect();
		super.step();
		System.out.println("recover check");
		recover();
		
	}

}
