package cS310_Project;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
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
	@Watch(watcheeClassName = "cS310_Project.RnumberCalc",
			watcheeFieldNames = "socialDistance1",
			query = "colocated",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void socialDistance(RnumberCalc Rnum) {
		if (Rnum.getSocialDistance1() == true) {
			setSpeed(0.03);
		} else {
			setSpeed(0.14);
		}
	}

	@ScheduledMethod(start = 1, interval = 1, priority = 5)
	public void step() {
		//Get grid location
		super.step();
	}

}
