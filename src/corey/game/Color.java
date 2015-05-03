package corey.game;

public class Color {
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public float r;
	public float g;
	public float b;
	
	public static final Color White = new Color(1.0f, 1.0f, 1.0f);
	public static final Color Blue = new Color(0.0f, 0.0f, 1.0f);
	public static final Color Red = new Color(1.0f, 0.0f, 0.0f);
	public static final Color Green = new Color(0.0f, 1.0f, 0.0f);
	public static final Color Black = new Color(0.0f, 0.0f, 0.0f);
}
