package corey.game.MissleCommand;
import corey.game.GameEngine;

public class MissleCommand {
	public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.run(new MissleCommandState(gameEngine));
    } 
}