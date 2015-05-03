package corey.game;

import java.awt.geom.Point2D;

public interface Translateable {
	void translate(double x, double y);
	Point2D getLocation();
}
