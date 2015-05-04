package corey.game.MissleCommand;

import java.awt.geom.Point2D;

import corey.game.Collideable;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.Vector;

public class EnemyMissle extends GameObject {

	public EnemyMissle(String name, GameEngine gameEngine, Point2D location, Point2D target) {
		super(name, gameEngine, location, defaultHeight, defaultWidth);
		velocity = Vector.FromPoints(location, target).setMagnitude(speed);
		initialLocation = location;
	}
	
	private static final double defaultHeight = .01;
	private static final double defaultWidth = .01;
	private static final double speed = .2;
	
	private Point2D initialLocation; 
	
	@Override
	public void draw() {
		MissleCommandDrawHelpers.drawMissle(getBoundingBox(), initialLocation, location);
	}

	@Override
	public void onCollide(Collideable otherCollideable) {
		if(otherCollideable instanceof Ground) {
			gameEngine.removeGameObject(this);
		}
		else if(otherCollideable instanceof Base) {
			gameEngine.removeGameObject(this);
			((MissleCommandState)gameEngine.getProfile()).baseHit((Base)otherCollideable);
		}
	}
}
