package corey.game;

public abstract class GameState implements SignalHandler, InputReceiver, Updateable {
	public GameState(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	protected GameEngine gameEngine; 
	protected double lifespan = 0.0;
	
	public void setupState() {}
	
	@Override
	public void handleSignal(SignalHandler from, String message) {}
	
	@Override
	public void updatePreCollision(float secondsSinceLastUpdate) {
		lifespan += secondsSinceLastUpdate;
	}
	
	@Override
	public void updatePostCollision() {}
	
	@Override
	public double getLifespan() {
		return lifespan;
	}
	
	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {}
}
