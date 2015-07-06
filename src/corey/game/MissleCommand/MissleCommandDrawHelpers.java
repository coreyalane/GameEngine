package corey.game.MissleCommand;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import corey.game.Color;
import corey.game.DrawHelpers;

public class MissleCommandDrawHelpers {
	public static void drawMissle(Rectangle2D tip, Point2D from, Point2D to) {
		Color tipColor = Color.White;
		Random rng = new Random();
		switch(rng.nextInt(3)) {
		case 0:
			tipColor = Color.Red;
			break;
		case 1:
			tipColor = Color.Yellow;
			break;
		case 2:
			tipColor = Color.White;
			break;
		}
		DrawHelpers.drawBoundingBox(tip, tipColor);
		DrawHelpers.drawLine(from, to, Color.Red);
	}
}
