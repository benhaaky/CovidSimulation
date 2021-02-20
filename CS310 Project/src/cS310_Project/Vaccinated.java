package cS310_Project;

import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Vaccinated extends Agent{
	private boolean immune;
	public Vaccinated(ContinuousSpace<Object> space, Grid<Object> grid) {
		// TODO Auto-generated constructor stub
		super(space, grid);
		double random = Math.random();
		System.out.println(random);
		if (random > 0.9) {
			this.immune = false;
			
		} else {
			this.immune = true;
			System.out.println("Immunity");
		}
	}
	public boolean isImmune() {
		return this.immune;
	}
	@ScheduledMethod(start = 1, interval = 1, shuffle=true, priority=5)
	public void step() {
		//Get grid location
		super.step();
		
	}

}
