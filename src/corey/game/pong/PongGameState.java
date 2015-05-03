package corey.game.pong;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.geom.Point2D;

import corey.game.GameEngine;
import corey.game.GameState;
import corey.game.SignalHandler;


public class PongGameState extends GameState {

	public PongGameState(GameEngine gameEngine) {
		super(gameEngine);
	}
	
	public PongGameState(GameEngine gameEngine, int player1Points, int player2Points) {
		super(gameEngine);
		this.player1Points = player1Points;
		this.player2Points = player2Points;
	}
	
	private static final int pointsToWin = 10;
	
	private int player1Points = 0;
	private int player2Points = 0;
	
	@Override
	public void setupState() {
		gameEngine.addGameObject(new Paddle("paddle1", gameEngine, new Point2D.Double(-0.9, -.1), new PaddleControlMapping(GLFW_KEY_W, GLFW_KEY_S)));
		gameEngine.addGameObject(new Paddle("paddle2", gameEngine, new Point2D.Double(0.9, -.1), new PaddleControlMapping(GLFW_KEY_UP, GLFW_KEY_DOWN)));
		
		Border leftBorder = new Border("leftBorder", gameEngine, new Point2D.Double(-.99, -0.1), 1.8, .01);
		Border rightBorder = new Border("rightBorder", gameEngine, new Point2D.Double(.99, -0.1), 1.8, .01);
		Border bottomBorder = new Border("bottomBorder", gameEngine, new Point2D.Double(0.0, -.99), .01, 2.0);
		Border topBorder = new Border("topBorder", gameEngine, new Point2D.Double(0.0, 0.65), .01, 2.0);
		leftBorder.setVisability(false);
		rightBorder.setVisability(false);
		bottomBorder.setVisability(false);
		topBorder.setVisability(true);		
		gameEngine.addGameObject(leftBorder);
		gameEngine.addGameObject(rightBorder);
		gameEngine.addGameObject(bottomBorder);
		gameEngine.addGameObject(topBorder);
		
		gameEngine.addGameObject(new Ball("ball", gameEngine, new Point2D.Double(0.0, -.1)));
		
		gameEngine.addGameObject(new ScoreBoard("scoreBoard1", gameEngine, new Point2D.Double(-.5, .85), player1Points));
		gameEngine.addGameObject(new ScoreBoard("scoreBoard2", gameEngine, new Point2D.Double(.5, .85), player2Points));
	}

	@Override
	public String getName() {
		return "MainGameState";
	}

	@Override
	public void handleSignal(SignalHandler from, String message) {
		if(message == "ball collision") {
			boolean pointScored = false;
			if(from.getName() == "leftBorder") {
				player2Points++;
				pointScored = true;
			}
			else if(from.getName() == "rightBorder") {
				player1Points++;
				pointScored = true;
			}
			if(pointScored) {
				if((player1Points < pointsToWin) && (player2Points < pointsToWin)) {
					gameEngine.setProfile(new PongGameState(gameEngine, player1Points, player2Points));
				}
				else {
					gameEngine.setProfile(new PongGameState(gameEngine, 0, 0));
				}
			}
		}
	}

}
