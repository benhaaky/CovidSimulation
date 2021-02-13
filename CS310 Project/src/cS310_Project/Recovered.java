package cS310_Project;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Recovered extends Agent {

	public Recovered(ContinuousSpace<Object> space, Grid<Object> grid, Infected oldAgent) {
		super(space, grid);
		Context<Object> context = ContextUtils.getContext(this);
		System.out.println(oldAgent);
		//context.remove(oldAgent);
		oldAgent = null;
		// TODO Auto-generated constructor stub
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		//Get grid location
		
		super.step();
	}


	
}
