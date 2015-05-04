package corey.game.MissleCommand;

import java.awt.geom.Point2D;

import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.RectangleHelpers;
import corey.game.Vector;

public class PlayerMissle extends GameObject {

	public PlayerMissle(String name, GameEngine gameEngine, Point2D location, Point2D target) {
		super(name, gameEngine, location, defaultHeight, defaultWidth);
		this.initialLocation = location;
		this.targetLocation = target;
		setVelocity(Vector.FromPoints(initialLocation, targetLocation).setMagnitude(speed));
	}

	private static final double defaultHeight = .01;
	private static final double defaultWidth = .01;
	private static final double speed = .2;
	
	private Point2D initialLocation;
	private Point2D targetLocation;
	
	@Override
	public void draw() {
		MissleCommandDrawHelpers.drawMissle(getBoundingBox(), initialLocation, location);
		DrawHelpers.drawX(RectangleHelpers.RectFromCenterPoint(targetLocation, .05, .05), Color.Green);
	}
	
	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		super.updatePreCollision(secondsSinceLastUpdate);
		double distanceForExplosion = velocity.magnitude() / 2.0 * secondsSinceLastUpdate;
		if(location.distance(targetLocation) < distanceForExplosion) {
			gameEngine.removeGameObject(this);
			gameEngine.addGameObject(new Explosion("explosion", gameEngine, location));
		}
	}
}
