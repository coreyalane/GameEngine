package corey.game.binary;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

import corey.game.GameEngine;
import corey.game.GameState;

public class BinaryGameState extends GameState {

	public BinaryGameState(GameEngine gameEngine) {
		super(gameEngine);
	}
	
	private static final double MAX_ENV_SPAWN_TIME = 60;
	
	private static Random rng = new Random();
	
	public int nWorkers = 0;
	public long nFrame = 0;
	public double totalTime = 0.0;
	public boolean spacePressed = false;
	
	public RegionInfo regionInfo = new RegionInfo();
	public ArrayList<Environment> environments = new ArrayList<Environment>();
	public Environment originalHeatsource;
	public Environment originalColdsource;
	
	private double timeUntilNextEnvironment = 0.0;
	private int restartCount = 0;

	@Override
	public int gameWidth() { return 2000; }
	@Override
	public int gameHeight() { return 1000; }
	
	@Override
	public String getName() {
		return "BinaryGameState";
	}
	
	@Override
	public void setupState() {
		super.setupState();
	}
	
	private static double rngLocation() {
		return ((rng.nextDouble()*2) - 1);
	}
	
	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {
		super.onInput(window, key, scancode, action, mods);
		if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
			spacePressed = true;
		}
		if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
			spacePressed = false;
		}
	}
	
	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		super.updatePreCollision(secondsSinceLastUpdate);
		totalTime += secondsSinceLastUpdate;
		if(originalHeatsource == null) {
			originalHeatsource = new Environment("originalHeatSource", gameEngine, this, 2, 10000);
			environments.add(originalHeatsource);
			gameEngine.addGameObject(originalHeatsource);
			//originalColdsource = new Environment("originalColdSource", gameEngine, this, -1.0, 10000);
			//environments.add(originalColdsource);
			//gameEngine.addGameObject(originalColdsource);
		}
		if(nWorkers <= 0) {
			Worker worker = new Worker("worker", gameEngine, this, new Point2D.Double(rngLocation(), rngLocation()), new Worker.WorkerStats());
			worker.s.fertility += (.01 * (double)restartCount);
			//System.out.println(worker.s.fertility);
			gameEngine.addGameObject(worker);
			restartCount++;
		}
		timeUntilNextEnvironment -= secondsSinceLastUpdate;
		if((nWorkers > 1000) && (timeUntilNextEnvironment < 0)) {
			Environment newEnv = new Environment("env", gameEngine, this);
			gameEngine.addGameObject(newEnv);
			environments.add(newEnv);
			timeUntilNextEnvironment = rng.nextDouble() * MAX_ENV_SPAWN_TIME;
		}
		System.out.print(nWorkers);
		System.out.print("  ");
		System.out.println(secondsSinceLastUpdate);
	}

}
