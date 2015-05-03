package corey.game.MissleCommand;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.GameState;
import corey.game.SignalHandler;

public class MissleCommandState extends GameState {

	public MissleCommandState(GameEngine gameEngine) {
		super(gameEngine);
	}

	private static final int numberOfBases = 6;
	
	private double timeSinceStart = 0.0;
	private double makeEnemyAtThisSecond = 1.0;
	private ArrayList<BaseState> baseStates = new ArrayList<BaseState>();
	
	@Override
	public String getName() {
		return "MissleCommandState";
	}

	@Override
	public void setupState() {
		setupGround();	
		setupBases();
	}

	private void setupGround() {
		gameEngine.addGameObject(new Ground("ground", gameEngine));
	}

	private void setupBases() {
		double basesWidth = 1.5;
		double basesOffset = basesWidth / (numberOfBases - 1);
		double basesStart = -basesWidth/2;
		for(int i = 0; i<numberOfBases; ++i) {
			Point2D baseLocation = new Point2D.Double(basesStart + basesOffset*i, -.875);
			gameEngine.addGameObject(new Base("base" + Integer.toString(i), gameEngine, baseLocation));
			baseStates.add(new BaseState(baseLocation));
		}
	}

	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		super.updatePreCollision(secondsSinceLastUpdate);
		timeSinceStart += secondsSinceLastUpdate;
		if(timeSinceStart > makeEnemyAtThisSecond) {
			launchNewEnemyMissle();
		}
	}

	private void launchNewEnemyMissle() {
		Random rng = new Random();
		Point2D missleTargetLocation = baseStates.get(rng.nextInt(baseStates.size())).location;
		double range = 1.5;
		double missleStartX = rng.nextDouble()*range - (range/2);
		gameEngine.addGameObject(new EnemyMissle("missle", gameEngine, new Point2D.Double(missleStartX, 1.0), missleTargetLocation));
		double attackRate = 3.0;
		double attackVariance = 1.0;
		makeEnemyAtThisSecond += (attackRate + ((rng.nextDouble() - .5)*attackVariance)); 
	}
}
