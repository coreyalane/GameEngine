package corey.game.binary;

import java.awt.geom.Point2D;
import java.util.Random;

import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.RectangleHelpers;

public class Environment extends GameObject {

	public Environment(String name, GameEngine gameEngine, BinaryGameState gs) {
		super(name, gameEngine, new Point2D.Double(rngLocation(), rngLocation()), .2, .1);
		this.gs = gs;
		timeLeft = rng.nextDouble() * MAX_TIME_LEFT;
		magnitude = (rng.nextDouble() * 2) - 1; //[-1 to 1]
		gs.regionInfo.AddHeat(location, magnitude);
	}
	
	public Environment(String name, GameEngine gameEngine, BinaryGameState gs, double magnitude, double timeout) {
		super(name, gameEngine, new Point2D.Double(rngLocation(), rngLocation()), .2, .1);
		this.gs = gs;
		timeLeft = timeout;
		this.magnitude = magnitude;
		gs.regionInfo.AddHeat(location, magnitude);
	}

	private static Random rng = new Random();
	private static double MAX_TIME_LEFT = 600;
	
	private BinaryGameState gs;
	private double timeLeft;
	private double magnitude;
	
	public double heatFactorAtDistance(Point2D worker) {
		double distanceFactor = Math.max(1 - worker.distance(location), 0.0); //[0 - 1]
		return magnitude * distanceFactor; //[-1 to 1]
	}
	
	private static double rngLocation() {
		return ((rng.nextDouble()*2) - 1);
	}
	
	@Override
	public void draw() {
		if(gs.spacePressed) {
			Color color = Color.Red;
			if(magnitude < 0) {
				color = Color.Blue;
			}
			DrawHelpers.drawX(RectangleHelpers.expand(getBoundingBox(), Math.abs(magnitude)), color);
		}
	}
	
	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		// TODO Auto-generated method stub
		super.updatePreCollision(secondsSinceLastUpdate);
		
		timeLeft -= secondsSinceLastUpdate;
		if(timeLeft <= 0) {
			gs.regionInfo.RemoveHeat(location, magnitude);
			gs.environments.remove(this);
			gameEngine.removeGameObject(this);
		}
	}
}
