package corey.game.pong;
import java.awt.geom.Point2D;

import corey.game.Collideable;
import corey.game.Color;
import corey.game.DrawHelpers;
import corey.game.GameEngine;
import corey.game.GameObject;
import corey.game.SignalHandler;


public class Border extends GameObject {

	Border(String name, GameEngine gameEngine, Point2D location, double height, double width) {
		super(name, gameEngine, location, height, width);
	}
	
	Color color = Color.White;

	@Override
	public void draw() {
		if(GlobalSettings.Instance.forceDrawBorders){
			color = Color.Green;
		}
		DrawHelpers.drawBoundingBox(getBoundingBox(), color);
	}
	
	@Override
	public void setVisability(boolean isVisible) {
		if(!GlobalSettings.Instance.forceDrawBorders){
			super.setVisability(isVisible);
		}
		else {
			super.setVisability(true);
		}
	}

	@Override
	public void onInput(long window, int key, int scancode, int action, int mods) {}

	@Override
	public void onCollide(Collideable otherCollideable) {}

	@Override
	public boolean isStatic() {
		return true;
	}
	
	@Override
	public void handleSignal(SignalHandler from, String message) {
		if((from.getName() == "ball") && (message == "collision")) {
			gameEngine.sendSignal(this, "MainGameState", "ball collision");
		}
	}

}
