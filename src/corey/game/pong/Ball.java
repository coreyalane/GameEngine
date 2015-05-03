package corey.game.pong;
import java.awt.geom.Point2D;
import java.util.Random;

import corey.game.CollideHelpers;
import corey.game.Collideable;
import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.Vector;


public class Ball extends GameObject {

	Ball(String name, GameEngine gameEngine, Point2D location) {
		super(name, gameEngine, location, defaultHeight, defaultWidth);
		Random rng = new Random();
		double startAngle = (rng.nextDouble() * 60) - 30;
		velocity = Vector.FromPolar(startAngle, defaultSpeed);
		if(rng.nextBoolean()) {
			velocity = velocity.rotate(180);
			location.setLocation(location.getX() + .65, location.getY());
		}
		else {
			location.setLocation(location.getX() - .65, location.getY());
		}
	}
	
	private static final double defaultHeight = .1;
	private static final double defaultWidth = .1;
	private static final double defaultSpeed = 2.0;
	private static final double speedMultiplier = 1.01;
	private static final double speedAdder = .01;
	
	@Override
	public void draw() {
		DrawHelpers.drawBoundingBox(getBoundingBox(), Color.Red);
	}

	@Override
	public void onCollide(Collideable otherCollideable) {
		CollideHelpers.Bounce(this, otherCollideable);
		increaseBallSpeed();
		jitterBallDirection();
		otherCollideable.handleSignal(this, "collision");
	}

	private void jitterBallDirection() {
		Random rng = new Random();
		velocity = velocity.rotate(rng.nextDouble()*30-15);
		setMaxBoundsForBallVelocity();
	}

	private void setMaxBoundsForBallVelocity() {
		double angle = velocity.angle();
		if((angle >= 45) && (angle <= 90)) {
			velocity = Vector.FromPolar(45, velocity.magnitude());
		}
		else if((angle >= 90) && (angle <= 135)) {
			velocity = Vector.FromPolar(135, velocity.magnitude());
		}
		else if((angle >= 225) && (angle <= 270)) {
			velocity = Vector.FromPolar(225, velocity.magnitude());
		}
		else if((angle >= 270) && (angle <= 315)) {
			velocity = Vector.FromPolar(315, velocity.magnitude());
		}
	}

	private void increaseBallSpeed() {
		velocity = velocity.setMagnitude((velocity.magnitude()*speedMultiplier)+speedAdder);
	}

	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {	}
}
