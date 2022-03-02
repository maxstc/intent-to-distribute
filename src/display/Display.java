package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
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
	
	private int fps;
	private int ups;
	private long frameTime;
	private long updateTime;
	
	public Display(int width, int height, Game game) {
		this.game = game;
		setPreferredSize(new Dimension(width, height));
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
	 * Resets the renderer (when a new map is created)
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

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        renderer.render(g);
        
        drawUpdateData(g);

        g.dispose();
        bufferStrategy.show();
    }
	
	/**
	 * Draws the frames per second, updates per second, frame time (in nanoseconds), update time (in nanoseconds), and the tick progress
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
