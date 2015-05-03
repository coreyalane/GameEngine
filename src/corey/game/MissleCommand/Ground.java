package corey.game.MissleCommand;

import java.awt.geom.Point2D;

import corey.game.Collideable;
import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;

public class Ground extends GameObject {

	public Ground(String name, GameEngine gameEngine) {
		super(name, gameEngine, defaultLocation, defaultHeight, defaultWidth);
		// TODO Auto-generated constructor stub
	}
	
	private static final Point2D defaultLocation = new Point2D.Double(0.0, -.95);
	private static final double defaultHeight = 0.1;
	private static final double defaultWidth = 2.0;

	@Override
	public void draw() {
		DrawHelpers.drawBoundingBox(getBoundingBox(), Color.Brown);
	}

	@Override
	public void onCollide(Collideable otherCollideable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {
		// TODO Auto-generated method stub

	}

}
