package corey.game.MissleCommand;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

import corey.game.GameEngine;
import corey.game.GameState;

public class MissleCommandState extends GameState {

	public MissleCommandState(GameEngine gameEngine) {
		super(gameEngine);
	}

	private static final int numberOfBases = 6;
	
	private double timeSinceStart = 0.0;
	private double makeEnemyAtThisSecond = 1.0;
	private ArrayList<Base> baseStates = new ArrayList<Base>();
	
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
			Base base = new Base("base" + Integer.toString(i), gameEngine, baseLocation, i);
			gameEngine.addGameObject(base);
			baseStates.add(base);
		}
	}

	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		super.updatePreCollision(secondsSinceLastUpdate);
		timeSinceStart += secondsSinceLastUpdate;
		if((timeSinceStart > makeEnemyAtThisSecond) && !baseStates.isEmpty()) {
			launchNewEnemyMissle();
		}
	}

	private void launchNewEnemyMissle() {
		Random rng = new Random();
		Point2D missleTargetLocation = baseStates.get(rng.nextInt(baseStates.size())).getLocation();
		double range = 1.5;
		double missleStartX = rng.nextDouble()*range - (range/2);
		gameEngine.addGameObject(new EnemyMissle("missle", gameEngine, new Point2D.Double(missleStartX, 1.0), missleTargetLocation));
		double attackRate = 3.0;
		double attackVariance = 1.0;
		makeEnemyAtThisSecond += (attackRate + ((rng.nextDouble() - .5)*attackVariance)); 
	}
	
	public void baseHit(Base base) {
		gameEngine.removeGameObject(base);
		baseStates.remove(base);
	}
	
	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {
		super.onInput(window, key, scancode, action, mods);
		if((key == GLFW_MOUSE_BUTTON_LEFT) && (action == GLFW_PRESS)) {
			launchPlayerMissle();
		}
	}

	private void launchPlayerMissle() {
		if(!baseStates.isEmpty()){
			Point2D mousePosition = gameEngine.getMousePosition();
			Point2D baseToFireFrom = getBaseToFireFrom(mousePosition);
			gameEngine.addGameObject(new PlayerMissle("playerMissle", gameEngine, baseToFireFrom, mousePosition));
		}
	}

	private Point2D getBaseToFireFrom(Point2D mousePosition) {
		Point2D baseToFireFrom = null;
		for(Base base : baseStates) {
			if(baseToFireFrom == null){
				baseToFireFrom = base.getLocation();
			}
			else {
				if(base.getLocation().distance(mousePosition) < baseToFireFrom.distance(mousePosition)) {
					baseToFireFrom = base.getLocation();
				}
			}
		}
		return baseToFireFrom;
	}
}
