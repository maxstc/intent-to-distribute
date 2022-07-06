package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import engine.Game;

/**
 * This class contains methods that deal with graphics and calls the {@code Renderer}'s methods
 */
public class Display extends Canvas {
	
	private static final long serialVersionUID = -3925535296884505775L;
	 
	private Game game;
	private Renderer renderer;
	
	private int fps; //frames per second
	private int ups; //updates per second
	private long frameTime; //time taken to render a frame (for statistics)
	private long updateTime; //time taken to update (for statistics)
	
	public Display(Game game) {
		this.game = game;
		setFocusable(false);
		fps = 0;
		ups = 0;
		renderer = new Renderer(game);
	}
	
	/**
	 * Creates a buffer strategy to reduce jitter
	 */
	public void createBuffer() {
		createBufferStrategy(3);
	}
	
	/**
	 * Resets the renderer (if a new map is created, this is necessary)
	 */
	public void resetRenderer() {
		renderer = new Renderer(game);
	}
	
	/**
	 * Renders the current game data
	 */
	public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();

        renderer.render(g, getWidth(), getHeight());
        
        //renders statistics
        drawUpdateData(g);

        g.dispose();
        bufferStrategy.show();
    }
	
	/**
	 * Draws the statistics: frames per second, updates per second, frame time (in nanoseconds), update time (in nanoseconds), and the tick progress
	 * @param g
	 */
	public void drawUpdateData(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(5, 5, 100, 100);
		g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 12));
		g.setColor(Color.GREEN);
        g.drawString("FPS: " + fps, 10, 20);
        g.drawString("UPS: " + ups, 10, 40);
        g.drawString("TICK: ", 10, 60);
        g.drawRect(42, 50, 20, 10);
        g.fillRect(42, 50, (int) (game.getTickProgress() * 20), 10);
        g.drawString("FTn: " + frameTime, 10, 80);
        g.drawString("UTn: " + updateTime, 10, 100);
	}
	
	/**
	 * Receives the data for drawUpdateData
	 * @param fps frames per second
	 * @param ups updates per second
	 * @param frameTime nanoseconds taken to render
	 * @param updateTime nanoseconds taken to update
	 */
	public void giveStats(int fps, int ups, long frameTime, long updateTime) {
		this.fps = fps;
		this.ups = ups;
		this.frameTime = frameTime;
		this.updateTime = updateTime;
	}
	
}
