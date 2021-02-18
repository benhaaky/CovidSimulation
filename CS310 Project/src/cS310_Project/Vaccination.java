package cS310_Project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Vaccination {
	private int vaccPerTick;
	public Vaccination(int num) {
		vaccPerTick = num;
	}
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void step() {
		randomVaccinate();
		
	}
	public void randomVaccinate() {
		Context<Object> context = ContextUtils.getContext(this);
		List<Susceptible> susAgents = new ArrayList<Susceptible>();
		Iterable<Object> susceptible = context.getRandomObjects(Susceptible.class, this.vaccPerTick);
		for (Object obj : susceptible) {
			// Check if the agent can be infected
			susAgents.add((Susceptible) obj);
			
		}
		for (Susceptible sus : susAgents) {
			// Replace susceptible agent with infected agent
			sus.getVaccine();
		}
	}
}