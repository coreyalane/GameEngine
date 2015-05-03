package corey.game;

public interface SignalHandler {
	String getName();
	void handleSignal(SignalHandler from, String message);
}
