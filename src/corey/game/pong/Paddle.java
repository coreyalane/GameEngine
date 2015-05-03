package corey.game.pong;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import corey.game.CollideHelpers;
import corey.game.Collideable;
import corey.game.Color;
import corey.game.ControlMapping;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.Vector;

public class Paddle extends GameObject {

	public Paddle(String name, GameEngine gameEngine, Point2D location, ControlMapping controlMapping) {
		super(name, gameEngine, location, defaultHeight, defaultLength);
		this.controlMapping = controlMapping;
	}
	
	final static double defaultHeight = .4;
	final static double defaultLength = .1;
	
	ControlMapping controlMapping;
	double moveSpeed = 4.0;
	
	@Override
	public void draw() {
		Rectangle2D boundingBox = getBoundingBox();
		DrawHelpers.drawBoundingBox(boundingBox, Color.White);
	}

	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {
		switch(controlMapping.mapInput(window, key, scancode, action, mods)) {
		case "startMoveUp":
			velocity.y = moveSpeed;
			break;
		case "startMoveDown":
			velocity.y = -moveSpeed;
			break;
		case "stop":
			velocity.y = 0;
			break;
		}
	}

	@Override
	public void onCollide(Collideable otherCollideable) {
		CollideHelpers.Bounce(this, otherCollideable);
		velocity = Vector.Zero();
	}
}
