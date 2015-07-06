package corey.game.binary;

import corey.game.GameEngine;

public class Binary {
	public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.enableCollisions(false);
        gameEngine.run(new BinaryGameState(gameEngine));
    } 
}
