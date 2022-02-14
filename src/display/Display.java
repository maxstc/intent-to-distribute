package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import engine.Game;

public class Display extends Canvas {
	
	private Game game;
	private Renderer renderer;
	
	private int fps;
	private int ups;
	
	public Display(int width, int height, Game game) {
		this.game = game;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		fps = 0;
		ups = 0;
		renderer = new Renderer(game);
	}
	
	public void createBuffer() {
		createBufferStrategy(3);
	}
	
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
	
	public void drawUpdateData(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(5, 5, 65, 62);
		g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 12));
		g.setColor(Color.GREEN);
        g.drawString("FPS: " + fps, 10, 20);
        g.drawString("UPS: " + ups, 10, 40);
        g.drawString("TICK: ", 10, 60);
        g.drawRect(42, 50, 20, 10);
        g.fillRect(42, 50, (int) (game.getTickProgress() * 20), 10);
	}
	
	public void giveStats(int fps, int ups) {
		this.fps = fps;
		this.ups = ups;
	}
	
}
