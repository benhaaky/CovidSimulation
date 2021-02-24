package cS310_Project;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Susceptible extends Agent {
	
	public Susceptible(ContinuousSpace<Object> space, Grid<Object> grid, boolean vulnerable) {
		super(space, grid, vulnerable);
		// TODO Auto-generated constructor stub
	}
	public void getVaccine() {
		GridPoint GridPoint = getGrid().getLocation(this);
		NdPoint spacePoint = getSpace().getLocation(this);
		Context<Object> context = ContextUtils.getContext(this);
		
		Vaccinated newVaccinated = new Vaccinated(this.getSpace(), this.getGrid(), atRisk());
		newVaccinated.setDestination(this.getDestination());
		context.add(newVaccinated);
		getSpace().moveTo(newVaccinated, spacePoint.getX(), spacePoint.getY());
		getGrid().moveTo(newVaccinated, GridPoint.getX(), GridPoint.getY());
		context.remove(this);
	}

	@ScheduledMethod(start = 1, interval = 1, priority = 5)
	public void step() {
		//Get grid location
		super.step();
	}

}
