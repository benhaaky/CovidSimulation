import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

/**
 * 
 */

/**
 * @author ben
 *
 */
public class Agent {
	
	//Store agent co-ordinate position decimal
	private ContinuousSpace<Object> space;
	//Store agent co-ordinate as grid position to find neighbouring agents
	private Grid<Object> grid;
	
	private GridPoint moveTowards;
	public boolean susceptible;
	private boolean infected;
	private boolean recovered;
	
	
	
	public Agent (ContinuousSpace<Object> space, Grid<Object> grid, boolean inf, boolean sus) {
		this.space = space;
		this.grid = grid;
		this.recovered = false;
		this.infected = inf;
		this.susceptible = sus;
		
		
		
	}
	
	public boolean susceptible() {
		if (susceptible == true) {
			return true;
		}
		return false;
	}
	public void getInfected() {
		this.susceptible = false;
		this.infected = true;
		System.out.println("Infected");
	}
	
	public void move() {
		GridPoint pt = this.moveTowards;
		GridPoint currentGrid = grid.getLocation(this);

		
		if((pt == null) || (grid.getDistance(pt, currentGrid) < 2)) {
			GridDimensions dimension = this.grid.getDimensions();
			
			int x = (int)(Math.random() * (dimension.getWidth() + 1));
			int y = (int)(Math.random() * (dimension.getHeight() + 1));
			this.moveTowards = new GridPoint(x, y);			
			
		} else {
			NdPoint myPoint = space.getLocation(this);
			NdPoint moveTowards = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, moveTowards);
			space.moveByVector(this, 0.1, angle, 0);
			NdPoint point = space.getLocation(this);
			grid.moveTo(this, (int)point.getX(), (int)point.getY());
		}
		
		
		
		
	}
	public void infect() {
		GridPoint pt = grid.getLocation(this);
		List<Agent> agents = new ArrayList<Agent>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			agents.add((Agent) obj);
			Agent agent = agents.get(agents.size()-1);
			if (agent.susceptible()) {
				agent.getInfected();
				
			}
		}
	}
	@ScheduledMethod(start = 1, interval = 1000)
	public void step() {
		//Get grid location
		if(infected == true) {
			infect();
		}
		move();
	}
}