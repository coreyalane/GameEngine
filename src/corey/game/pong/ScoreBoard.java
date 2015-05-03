package corey.game.pong;
import java.awt.geom.Point2D;

import corey.game.Collideable;
import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.RectangleHelpers;


public class ScoreBoard extends GameObject {

	ScoreBoard(String name, GameEngine gameEngine, Point2D location, int points) {
		super(name, gameEngine, location, defaultHeight, defaultWidth);
		this.points = points;
	}

	private static final double defaultHeight = .25;
	private static final double defaultWidth = .75;
	private int points = 0;
	
	@Override
	public void draw() {
		DrawHelpers.drawBoundingBox(getBoundingBox(), Color.White);
		DrawHelpers.drawBoundingBox(RectangleHelpers.expand(getBoundingBox(), .9), Color.Black);
		double xBaseline = getBoundingBox().getX() + 2*width/16;
		double yBaseline = getBoundingBox().getY() + height/2;
		double xOffset = width/16;
		for(int i=0; i<points; ++i) {
			DrawHelpers.drawBoundingBox(RectangleHelpers.RectFromCenterPoint(new Point2D.Double(xBaseline + (i*xOffset), yBaseline), .025, .15), Color.White);
		}
	}

	@Override
	public void onCollide(Collideable otherCollideable) {}
	
	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {}

}
