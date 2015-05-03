package corey.game;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class RectangleHelpers {
	public static Point2D UL(Rectangle2D rect){
		return new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight());
	}
	
	public static Point2D UR(Rectangle2D rect){
		return new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
	}
	
	public static Point2D LL(Rectangle2D rect){
		return new Point2D.Double(rect.getX(), rect.getY());
	}
	
	public static Point2D LR(Rectangle2D rect){
		return new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY());
	}
	
	public static Rectangle2D expand(Rectangle2D rect, double multiplier) {
		double oldWidth = rect.getWidth();
		double oldHeight = rect.getHeight();
		double newWidth = oldWidth * multiplier;
		double newHeight = oldHeight * multiplier;
		double newX = rect.getX() - ((newWidth - oldWidth)/2);
		double newY = rect.getY() - ((newHeight - oldHeight)/2);
		return new Rectangle2D.Double(newX, newY, newWidth, newHeight);
	}
	
	public static Rectangle2D RectFromCenterPoint(Point2D point, double width, double height) {
		double xOffset = width/2;
		double yOffset = height/2;
		return new Rectangle2D.Double(point.getX()-xOffset, point.getY()-yOffset, width, height);
	}
}
