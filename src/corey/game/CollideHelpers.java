package corey.game;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import corey.game.pong.Side;


public class CollideHelpers {
	public static void Bounce(Collideable toBounce, Collideable other) {
		Rectangle2D boundingBox = toBounce.getBoundingBox();
		Line2D leftSide = other.getEdge(Side.LEFT_SIDE);
		Line2D rightSide = other.getEdge(Side.RIGHT_SIDE);
		boolean leftOrRightCollide = false;
		if(boundingBox.intersectsLine(leftSide) || boundingBox.intersectsLine(rightSide)) {
			leftOrRightCollide = true;
		}
		boundingBox = toBounce.getBoundingBox();
		Line2D topSide = other.getEdge(Side.TOP_SIDE);
		Line2D bottomSide = other.getEdge(Side.BOTTOM_SIDE);
		boolean topOrBottomCollide = false;
		if(boundingBox.intersectsLine(topSide) || boundingBox.intersectsLine(bottomSide)) {
			topOrBottomCollide = true;
		}
		if(leftOrRightCollide || topOrBottomCollide) {
			toBounce.revertCollision();
			double xVelocity = toBounce.getVelocity().x;
			double yVelocity = toBounce.getVelocity().y;
			if(leftOrRightCollide){
				xVelocity = -xVelocity;
			}
			if(topOrBottomCollide){
				yVelocity = -yVelocity;
			}
			toBounce.setVelocity(new Vector(xVelocity, yVelocity));
		}
	}
}
