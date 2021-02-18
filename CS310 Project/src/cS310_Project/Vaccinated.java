package cS310_Project;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Vaccinated extends Agent{
	
	public Vaccinated(ContinuousSpace<Object> space, Grid<Object> grid) {
		// TODO Auto-generated constructor stub
		super(space, grid);
	}
	
	@ScheduledMethod(start = 1, interval = 1, shuffle=true, priority=5)
	public void step() {
		//Get grid location
		super.step();
		
	}

}
