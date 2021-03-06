package cS310_Project;

import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.SimUtilities;

public class SimBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		// TODO Auto-generated method stu
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		
		int size = params.getInteger("spaceSize");
		//Generate space for agents to be displayed
		ContinuousSpaceFactory spaceFactory =
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(),//Random location for agents
				new repast.simphony.space.continuous.WrapAroundBorders(), //Wrapped borders
				size, size);
		
		// Generate grid to detect collision between agents
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("Grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
				new SimpleGridAdder<Object>(),
				true, size, size));
		
		// Get the parameter input
		int agentCount = params.getInteger("numSusceptible");
		int infectedCount = params.getInteger("numInfected");
		GridPoint moveTowards;
		boolean inf = false;
		boolean sus = true;
		
		// Create susceptible agents and add them to context
		double atRisk = 0.2;
		double numAtRisk = agentCount*atRisk;
		System.out.println(numAtRisk);
		for (int i=0; i<agentCount; i++) {
			if (i<numAtRisk) {
				System.out.println("fdsfdsfndsjkfdskjfjkdsbfjdsjfdsfdssjs");
				context.add(new Susceptible(space, grid, true));
			} else {
				
				context.add(new Susceptible(space, grid, false));
			}
			
		}
		
		
		// Create infecte agents and add them to context
		for (int i=0; i<infectedCount; i++) {
			
			context.add(new Infected(space, grid, false, false));
			
		}
		context.add(new RnumberCalc());
		context.add(new Vaccination(10));
		context.add(new Hospital());
		// Move each agent
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
		}
		return context;
	}

}