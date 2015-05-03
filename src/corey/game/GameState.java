package corey.game;

public abstract class GameState implements SignalHandler, InputReceiver {
	public GameState(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	protected GameEngine gameEngine; 
	
	public abstract void setupState();
	
	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {}
}
