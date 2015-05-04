package corey.game;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.opengl.GL11;


public class DrawHelpers {
	public static void drawBoundingBox(Rectangle2D boundingBox, Color color) {
		float x = (float)boundingBox.getX();
		float y = (float)boundingBox.getY();
		float width = (float)boundingBox.getWidth();
		float height = (float)boundingBox.getHeight();
		
		glBegin(GL11.GL_QUADS);
        glColor3f(color.r, color.g, color.b);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();
	}
	
	public static void drawLine(Point2D start, Point2D end, Color color) {
		GL11.glColor3f(color.r, color.g, color.b);
	    GL11.glBegin(GL11.GL_LINE_STRIP);
	    GL11.glVertex2d(start.getX(), start.getY());
	    GL11.glVertex2d(end.getX(), end.getY());
	    GL11.glEnd();
	}
	
	public static void drawX(Rectangle2D boundingBox, Color color) {
		drawLine(RectangleHelpers.UL(boundingBox), RectangleHelpers.LR(boundingBox), color);
		drawLine(RectangleHelpers.UR(boundingBox), RectangleHelpers.LL(boundingBox), color);
	}
}
