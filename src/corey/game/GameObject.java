package corey.game;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import corey.game.pong.Side;


public abstract class GameObject implements Drawable, Translateable, Collideable, Updateable, InputReceiver {
	
	public GameObject(String name, GameEngine gameEngine, Point2D location, double height, double width) {
		this.name = name;
		this.gameEngine = gameEngine;
		this.location = location;
		this.height = height;
		this.width = width;
	}
	
	protected String name;
	protected GameEngine gameEngine;
	protected boolean visible = true;
	protected Point2D location;
	protected Point2D proposedLocation = null;
	protected double height;
	protected double width; 
	protected Vector velocity = Vector.Zero();
	
	@Override
	public void setVisability(boolean isVisible) {
		visible = isVisible;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void translate(double x, double y) {
		proposedLocation.setLocation(proposedLocation.getX() + x, proposedLocation.getY() + y);
	}
	
	@Override
	public Point2D getLocation() {
		if(proposedLocation != null){
			return (Point2D)proposedLocation.clone();
		}
		else {
			return (Point2D)location.clone();
		}
	}
	
	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		proposedLocation = (Point2D) location.clone();
		translate(secondsSinceLastUpdate * velocity.x, secondsSinceLastUpdate * velocity.y);
	}
	
	@Override
	public void updatePostCollision() {
		location = (Point2D) proposedLocation.clone();	
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void handleSignal(SignalHandler from, String message) { }
	
	@Override
	public Rectangle2D getBoundingBox() {
		return RectangleHelpers.RectFromCenterPoint(proposedLocation, width, height);
	};
	
	@Override
	public boolean isStatic() {
		return false;
	}
	
	@Override
	public double getBound(Side side) {
		double bound = 0.0;
		switch(side) {
		case LEFT_SIDE:
			bound = proposedLocation.getX() - (width/2);
		case RIGHT_SIDE:
			bound = proposedLocation.getX() + (width/2);
		case TOP_SIDE:
			bound = proposedLocation.getY() + (height/2);
		case BOTTOM_SIDE:
			bound = proposedLocation.getY() - (height/2);
		}
		return bound;
	}
	
	@Override
	public Line2D getEdge(Side side) {
		Line2D edge = new Line2D.Double();
		Rectangle2D boundingBox = getBoundingBox();
		Point2D UL = RectangleHelpers.UL(boundingBox);
		Point2D UR = RectangleHelpers.UR(boundingBox);
		Point2D LL = RectangleHelpers.LL(boundingBox);
		Point2D LR = RectangleHelpers.LR(boundingBox);
		switch(side) {
		case LEFT_SIDE:
			edge.setLine(UL, LL);
			break;
		case RIGHT_SIDE:
			edge.setLine(UR, LR);
			break;
		case TOP_SIDE:
			edge.setLine(UL, UR);
			break;
		case BOTTOM_SIDE:
			edge.setLine(LL, LR);
			break;
		}
		return edge;
	}
	
	
	@Override
	public Vector getVelocity() {
		return velocity;
	}
	
	@Override
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	@Override
	public Vector getMoment() {
		return new Vector(proposedLocation.getX() - location.getX(), proposedLocation.getY() - location.getY());
	}
	
	@Override
	public void revertCollision() {
		proposedLocation = (Point2D) location.clone();
	}
	
	@Override
	public void onCollide(Collideable otherCollideable) {}
	
	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {}
	
	@Override
	public void draw() {}
}
