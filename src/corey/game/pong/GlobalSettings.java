package corey.game.pong;

public class GlobalSettings {
	private GlobalSettings() {}
	
	public static GlobalSettings Instance = new GlobalSettings();
	
	public boolean forceDrawBorders = false;
}
