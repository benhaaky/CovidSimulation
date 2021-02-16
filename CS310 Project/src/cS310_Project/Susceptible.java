package cS310_Project;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Susceptible extends Agent {

	public Susceptible(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
		// TODO Auto-generated constructor stub
	}
	
	public void getInfected(Context<Object> context) {
		
		Infected newInfected = new Infected(getSpace(), getGrid());
		newInfected.setDestination(this.getDestination());
		context.add(newInfected);
		System.out.println("Infected");
	}
	@ScheduledMethod(start = 1, interval = 1, priority = 5)
	public void step() {
		//Get grid location
		
		super.step();
	}

}
