package corey.game.pong;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import corey.game.ControlMapping;


public class PaddleControlMapping implements ControlMapping {

	public PaddleControlMapping(int upKey, int downKey) {
		this.upKey = upKey;
		this.downKey = downKey;
	}
	
	private int upKey;
	private int downKey;
	
	@Override
	public String mapInput(long window, int key, int scancode, int action,
			int mods) {
		if ( key == upKey && action == GLFW_PRESS ) {
        	return "startMoveUp";
        }
		else if ( key == upKey && action == GLFW_RELEASE ) {
			return "stop";
		}
		else if ( key == downKey && action == GLFW_PRESS ) {
        	return "startMoveDown";
        }
		else if ( key == downKey && action == GLFW_RELEASE ) {
        	return "stop";
        }
		return "";
	}
}