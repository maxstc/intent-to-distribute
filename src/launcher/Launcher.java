package launcher;

import engine.Game;
import engine.Loop;

/**
 * This class contains a main method that starts the game
 */
public class Launcher {
	public static void main(String[] args) {
		new Thread(new Loop(new Game())).start();
	}
}
