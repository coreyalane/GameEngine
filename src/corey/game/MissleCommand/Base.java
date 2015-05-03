package corey.game.MissleCommand;

import java.awt.geom.Point2D;

import corey.game.Collideable;
import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;

public class Base extends GameObject {

	public Base(String name, GameEngine gameEngine, Point2D location, int number) {
		super(name, gameEngine, location, defaultHeight, defaultWidth);
		this.number = number;
	}

	private static final double defaultWidth = .1;
	private static final double defaultHeight = .05;
	
	private int number;
	
	public int getNumber() {
		return number;
	}
	
	@Override
	public void draw() {
		DrawHelpers.drawBoundingBox(getBoundingBox(), Color.Blue);
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
