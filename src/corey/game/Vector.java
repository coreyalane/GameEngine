package corey.game;

import java.awt.geom.Point2D;

public class Vector {
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(double xDir, double yDir, double magnitude) {
		Vector unitVector = new Vector(xDir, yDir).unitVector();
		this.x = unitVector.x * magnitude;
		this.y = unitVector.y * magnitude;
	}
	
	public static Vector FromPolar(double angle, double magnitude) {
		return new Vector(1.0, 0.0).setMagnitude(magnitude).rotate(angle);
	}
	
	public static Vector FromPoints(Point2D from, Point2D to) {
		return new Vector(to.getX() - from.getX(), to.getY() - from.getY());
	}
	
	public double x;
	public double y;
	
	public double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public double angle() {
		double angle = Math.atan2(y, x) * 180 / Math.PI;
		if(angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	public Vector unitVector() {
		double magnitude = magnitude();
		if(magnitude > 0) {
			return new Vector(x / magnitude, y / magnitude);
		}
		else {
			return Zero();
		}
	}
	
	public Vector setMagnitude(double magnitude) {
		Vector unitVector = this.unitVector();
		return new Vector(unitVector.x * magnitude, unitVector.y * magnitude);
	}
	
	public Vector rotate(double angle) {
		double radians = angle * Math.PI / 180.0;
		double newX = x*Math.cos(radians) - y*Math.sin(radians);
		double newY = x*Math.sin(radians) + y*Math.cos(radians);
		return new Vector(newX, newY);
	}
	
	public static Vector Zero() {
		return new Vector(0.0, 0.0);
	}
}
