package corey.game;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;


public class GameEngine {
	private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
	
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private ArrayList<GameObject> gameObjectsToRemove = new ArrayList<GameObject>();
	private GameState currentProfile = null;
	private GameState nextProfile = null;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private long window;
    private DoubleBuffer mouseXBuffer = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer mouseYBuffer = BufferUtils.createDoubleBuffer(1);
    private Point2D mousePosition = new Point2D.Double();
	
	private void init(GameState initialGameState) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
 
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
 
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
                }
                sendInputs(window, key, scancode, action, mods);;
            }
        });
        
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
        	@Override
        	public void invoke(long window, int button, int action, int mods) {
        		sendInputs(window, button, 0, action, mods);
        	}
        });
        
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (GLFWvidmode.width(vidmode) - WIDTH) / 2,
            (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(window);
        
     // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        GL11.glClearDepth(1);                                       
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glViewport(0,0,WIDTH,HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		 initializeGameState(initialGameState);
    }
	
	private void cacheMousePosition() {
		glfwGetCursorPos(window, mouseXBuffer, mouseYBuffer);
		double adjustedX = (mouseXBuffer.get(0) / WIDTH * 2.0) - 1.0;
		double adjustedY = -((mouseYBuffer.get(0) / HEIGHT * 2.0) - 1.0);
		mousePosition = new Point2D.Double(adjustedX, adjustedY);
	}
	
	public Point2D getMousePosition() {
		return (Point2D)mousePosition.clone();
	}
	
	public void run(GameState initialGameState) {
        try {
            init(initialGameState);
            loop();
 
            
        } finally {
            glfwDestroyWindow(window);
            keyCallback.release();
            mouseButtonCallback.release();
            glfwTerminate();
            errorCallback.release();
        }
    }
	
	private void loop() {               
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        long lastUpdateTime = getTimeMilliseconds();
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            long thisUpdateTime = getTimeMilliseconds();
        	glfwPollEvents();
            cacheMousePosition();
            update(getDelta(lastUpdateTime, thisUpdateTime));
            lastUpdateTime = thisUpdateTime;
            draw();           
            glfwSwapBuffers(window);
        }
    }
	
	private static long getTimeMilliseconds() {
        return System.nanoTime() / 1000000; 
    }
    
    private static float getDelta(long lastUpdateTime, long thisUpdateTime) {
    	return ((float)(thisUpdateTime-lastUpdateTime))/1000.0f;
    }
    
    private void initializeGameState(GameState initialGameState) {
    	setProfile(initialGameState);
    }
	
	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}
	
	public void removeGameObject(GameObject toRemove) {
		gameObjectsToRemove.add(toRemove);
	}
	
	public void draw() {
		for(Drawable drawable : gameObjects) {
			if(drawable.isVisible()){
				drawable.draw();
			}
		}
	}
	
	public void sendInputs(long window, int key, int scancode, int action, int mods) {
		currentProfile.onInput(window, key, scancode, action, mods);
		for(InputReceiver inputReceiver : gameObjects) {
			inputReceiver.onInput(window, key, scancode, action, mods);
		}
	}
	
	public void update(float secondsSinceLastUpdate) {
		if(nextProfile != null) {
			currentProfile = nextProfile;
			gameObjects.clear();
			currentProfile.setupState();
			nextProfile = null;
		}
		currentProfile.updatePreCollision(secondsSinceLastUpdate);
		for(Updateable updateable : gameObjects) {
			updateable.updatePreCollision(secondsSinceLastUpdate);
		}
		for(Collideable collideable : gameObjects) {
			for(Collideable otherCollideable : gameObjects) {
				if(!collideable.equals(otherCollideable)) {
					Rectangle2D collideableRect = collideable.getBoundingBox();
					Rectangle2D otherCollideableRect = otherCollideable.getBoundingBox();
					if(collideableRect.intersects(otherCollideableRect)) {
						collideable.onCollide(otherCollideable);
					}
				}
			}
		}
		currentProfile.updatePostCollision();
		for(Updateable updateable : gameObjects) {
			updateable.updatePostCollision();
		}
		for(GameObject toRemove : gameObjectsToRemove) {
			gameObjects.remove(toRemove);
		}
		gameObjectsToRemove.clear();
	}
	
	public void setProfile(GameState profile) {
		nextProfile = profile;
	}
	
	public GameState getProfile() {
		return currentProfile;
	}
	
	public void sendSignal(SignalHandler from, String to, String message) {
		if(currentProfile.getName() == to){
			currentProfile.handleSignal(from, message);
		}
		for(SignalHandler signalHandler : gameObjects) {
			if(signalHandler.getName() == to) {
				currentProfile.handleSignal(from, message);
			}
		}
	}
}
