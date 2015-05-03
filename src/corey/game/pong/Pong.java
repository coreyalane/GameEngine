package corey.game.pong;

import corey.game.GameEngine;
 
public class Pong {
    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.run(new PongGameState(gameEngine));
    } 
}
