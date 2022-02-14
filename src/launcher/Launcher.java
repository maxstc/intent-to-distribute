package launcher;

import engine.Game;
import engine.Loop;

public class Launcher {
	public static void main(String[] args) {
		new Thread(new Loop(new Game())).start();
	}
}
