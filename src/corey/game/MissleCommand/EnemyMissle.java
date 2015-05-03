package corey.game.MissleCommand;

import java.awt.geom.Point2D;
import java.util.Random;

import corey.game.Collideable;
import corey.game.Color;
import corey.game.DrawHelpers;
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
		Color tipColor = Color.White;
		Random rng = new Random();
		switch(rng.nextInt(3)) {
		case 0:
			tipColor = Color.Red;
			break;
		case 1:
			tipColor = Color.Yellow;
			break;
		case 2:
			tipColor = Color.White;
			break;
		}
		DrawHelpers.drawBoundingBox(getBoundingBox(), tipColor);
		DrawHelpers.drawLine(initialLocation, location, Color.Red);
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

	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {
		// TODO Auto-generated method stub

	}

}
