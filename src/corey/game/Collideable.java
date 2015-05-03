package corey.game;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import corey.game.pong.Side;


public interface Collideable extends SignalHandler {
	Rectangle2D getBoundingBox();
	void onCollide(Collideable otherCollideable);
	boolean isStatic();
	double getBound(Side side);
	Line2D getEdge(Side side);
	Vector getVelocity();
	Vector getMoment();
	void setVelocity(Vector velocity);
	void revertCollision();
}
