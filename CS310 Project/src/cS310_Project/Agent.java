package cS310_Project;
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

	//Store whether agents have been vaccinated
	private boolean vaccinated;
	
	//Store whether agent is vulnerable
	private boolean vulnerable;
	
	
	public Agent (ContinuousSpace<Object> space, Grid<Object> grid, boolean vul) {
		//Init agent in space and grid
		this.space = space;
		this.grid = grid;
		this.vaccinated = false;
		this.vulnerable = vul;	
		
	}
	public boolean atRisk() {
		return this.vulnerable;
	}
	public Grid<Object> getGrid(){
		return this.grid;
	}
	public ContinuousSpace<Object> getSpace(){
		return this.space;
	}
	public GridPoint getDestination() {
		return this.moveTowards;
	}
	public void setDestination(GridPoint dest) {
		this.moveTowards = dest;
	}
	
	// Move Agents
	public void move() {
		//Get current destination
		GridPoint pt = this.moveTowards;
		GridPoint currentGrid = grid.getLocation(this);

		// Check is agent has no destination or has reach theirs
		if((pt == null) || (grid.getDistance(pt, currentGrid) < 2)) {
			// Create new random location for agent to move towards
			GridDimensions dimension = this.grid.getDimensions();
			
			int x = (int)(Math.random() * (dimension.getWidth() + 1));
			int y = (int)(Math.random() * (dimension.getHeight() + 1));
			this.moveTowards = new GridPoint(x, y);
			
		} else {
			// Agent has not reached destination move agent in the direction of dest
			NdPoint myPoint = space.getLocation(this);
			NdPoint moveTowards = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, moveTowards);
			space.moveByVector(this, 0.1, angle, 0);
			NdPoint point = space.getLocation(this);
			grid.moveTo(this, (int)point.getX(), (int)point.getY());
		}
		
	}

	public void step() {
		//Get grid location
		move();
	}
}