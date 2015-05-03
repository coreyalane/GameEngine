package corey.game.MissleCommand;

import java.awt.geom.Point2D;

public class BaseState {
	public BaseState(Point2D location){
		this.location = location;
	}
	public Point2D location;
	public boolean alive = true;
}
