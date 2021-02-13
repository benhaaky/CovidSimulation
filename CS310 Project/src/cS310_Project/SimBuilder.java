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
		// TODO Auto-generated method stub
		ContinuousSpaceFactory spaceFactory =
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(),//Random location for agents
				new repast.simphony.space.continuous.WrapAroundBorders(), //Wrapped borders
				1000, 1000);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("Grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
				new SimpleGridAdder<Object>(),
				true, 1000, 1000));
		Parameters params = RunEnvironment.getInstance().getParameters();
		int agentCount = params.getInteger("numSusceptible");
		int infectedCount = params.getInteger("numInfected");
		GridPoint moveTowards;
		boolean inf = false;
		boolean sus = true;
		for (int i=0; i<agentCount; i++) {
			
			context.add(new Susceptible(space, grid));
			
		}
		for (int i=0; i<infectedCount; i++) {
			
			context.add(new Infected(space, grid));
			
		}
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
		}
		return context;
	}

}