package corey.game.MissleCommand;

import java.awt.geom.Point2D;
import java.util.Random;

import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;

public class Explosion extends GameObject {

	public Explosion(String name, GameEngine gameEngine, Point2D location) {
		super(name, gameEngine, location, startSize, startSize);
		// TODO Auto-generated constructor stub
	}
	
	private static final double startSize = .1;
	private static final double totalLifespan = 2.0;
	
	@Override
	public void draw() {
		super.draw();
		Color color = Color.White;
		if(new Random().nextBoolean()) {
			color = Color.Yellow;
		}
		DrawHelpers.drawBoundingBox(getBoundingBox(), color);
	}

	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		super.updatePreCollision(secondsSinceLastUpdate);
		if(lifespan > totalLifespan) {
			gameEngine.removeGameObject(this);
		}
	}
}
