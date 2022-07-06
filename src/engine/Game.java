package engine;

import java.awt.event.KeyEvent;

import display.Camera;
import display.MainFrame;
import gameObject.Tile;
import gameObject.TileMap;
import input.Input;

/**
 * This class connects the visual, interactive, and simulation parts of the game
 */
public class Game {
	
	private static int mapSize = 200; //The width and height of the map to be created
	
	private Tile selectedTile;
	
	private MainFrame mainFrame;
	private Input input;
	
	private TileMap tileMap;
	
	private int updatesPerTick = 120; //The default number of updates that a tick takes
	
	private int tickProgress = 0;
	private boolean ticking = true;
	
	private Camera camera;
	
	public Game() {
		camera = new Camera();
		tileMap = new TileMap(mapSize, mapSize);
		mainFrame = new MainFrame(800, 600, this);
		input = new Input(this);
		
		mainFrame.addMouseListener(input);
		mainFrame.addKeyListener(input);
		mainFrame.addMouseWheelListener(input);
		mainFrame.getDisplay().addMouseListener(input);
		mainFrame.getDisplay().addKeyListener(input);
		mainFrame.getDisplay().addMouseWheelListener(input);
		
		selectedTile = null;
	}
	
	public void update() {
		float dx = 0;
		float dy = 0;
		checkMapModes();
		if (input.isPressed(KeyEvent.VK_W)) {
			dy = -10 / camera.getZoom();
		}
		if (input.isPressed(KeyEvent.VK_S)) {
			dy = 10 / camera.getZoom();
		}
		if (input.isPressed(KeyEvent.VK_A)) {
			dx = -10 / camera.getZoom();
		}
		if (input.isPressed(KeyEvent.VK_D)) {
			dx = 10 / camera.getZoom();
		}
		camera.update(dx, dy, input.getMouseWheelRotation());
		if (ticking) {
			tickProgress++;
		}
		if (tickProgress == updatesPerTick) {
			tick();
			tickProgress = 0;
		}
	}
	
	public void checkMapModes() {
		if (input.isPressed(KeyEvent.VK_0)) { //height
			tileMap.updateMapMode(0);
		}
		if (input.isPressed(KeyEvent.VK_9)) { //temp
			tileMap.updateMapMode(1);
		}
		if (input.isPressed(KeyEvent.VK_8)) { //land
			tileMap.updateMapMode(2);
		}
		if (input.isPressed(KeyEvent.VK_7)) { //pop
			tileMap.updateMapMode(3);
		}
		if (input.isPressed(KeyEvent.VK_6)) { //civ
			tileMap.updateMapMode(4);
		}
	}
	
	/**
	 * Performs a game tick
	 * 
	 * Not yet implemented
	 */
	public void tick() {
		
	}
	
	/**
	 * Called when the user clicks on the screen.
	 * 
	 * Recreates the map.
	 */
	public void click(int x, int y, boolean isLeftClick) {
		float cartX = (x - mainFrame.getDisplay().getWidth() / 2) / camera.getZoom() + camera.getX();
		float cartY = (y - mainFrame.getDisplay().getHeight() / 2) / camera.getZoom() + camera.getY();
		
		int trueX = 0;
		int trueY = 0;
		
		//Determine the tile that was clicked on
		if (cartX >= 0 && cartY >= 0) {
			float testY = (float) (cartY / (Math.sqrt(3) / 2)) % 1;
			float testX = 2 * (cartX % 1.5f);
			
			if (cartX % 3 <= 0.5) {
				if ((cartY / Math.sqrt(3)) % 1 <= 0.5) {
					if (1 - testY > testX) {
						trueX--;
					}
				}
				else {
					if (testY > testX) {
						trueX--;
					}
				}
			}
			else if (cartX % 1.5 <= 0.5) {
				if ((cartY / Math.sqrt(3)) % 1 <= 0.5) {
					if (testY > testX) {
						trueX--;
					}
				}
				else {
					if (1 - testY > testX) {
						trueX--;
					}
				}
			}
		}
		
		trueX += (int) (cartX / 1.5f);
		
		if (trueX % 2 == 1) {
			cartY -= (Math.sqrt(3) / 2);
		}
		
		trueY = (int) (cartY / (Math.sqrt(3)));
		
		System.out.println(trueX + ", " + trueY);
		
		//Generates a new map
//		tileMap = new TileMap(mapSize, mapSize);
//		mainFrame.getDisplay().resetRenderer();
		
		selectedTile = tileMap.getTile(trueX, trueY);
		if (selectedTile != null) {
			selectedTile.click();
		}
		System.out.println(selectedTile);
		
	}
	
	/**
	 * Called when the user presses a button
	 * 
	 * Currently only changes the speed of ticks
	 */
	public void press(char c) {
		switch(c) {
			case (' '):
				ticking = !ticking;
				break;
			case ('1'):
				tickProgress = 60 * tickProgress / updatesPerTick;
				updatesPerTick = 60;
				break;
			case ('2'):
				tickProgress = 120 * tickProgress / updatesPerTick;
				updatesPerTick = 120;
				break;
			case ('3'):
				tickProgress = 240 * tickProgress / updatesPerTick;
				updatesPerTick = 240;
				break;
			case ('4'):
				tickProgress = 480 * tickProgress / updatesPerTick;
				updatesPerTick = 480;
				break;
			case ('5'):
				tickProgress = 960 * tickProgress / updatesPerTick;
				updatesPerTick = 960;
				break;
			default:
				//do nothing
				break;
		}
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public Tile[][] getTiles() {
		return tileMap.getTiles();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Tile getSelectedTile() {
		return selectedTile;
	}
	
	public float getTickProgress() {
		return (tickProgress / (float) updatesPerTick);
	}
	
	public TileMap getTileMap() {
		return tileMap;
	}
	
}
