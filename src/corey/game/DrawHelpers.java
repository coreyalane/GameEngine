package corey.game;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

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
}
