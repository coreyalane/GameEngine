package corey.game;

public interface InputReceiver {
	void onInput(long window, int key, int scancode, int action, int mods);
}
