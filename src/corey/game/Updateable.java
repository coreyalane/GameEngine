package corey.game;

public interface Updateable {
	void updatePreCollision(float secondsSinceLastUpdate);
	void updatePostCollision();
	double getLifespan();
}
