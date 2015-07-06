package corey.game.binary;

import java.util.Random;

public abstract class Choose {

	static Random rng = new Random();
	
	static boolean p(double prob) {
		if (rng.nextFloat() < prob) {
			return true;
		}
		return false;
	}
	
	static boolean Binary() {
		return p(.5);
	}

}
