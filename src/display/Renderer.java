package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import engine.Game;
import gameObject.Point;
import gameObject.Tile;

/**
 * This class handles most of the rendering, i.e. drawing the hexagons to the screen
 *
 */
public class Renderer {

	private Game game;
	private List<Tile> tiles;
	private List<Tile> visibleTiles;
	private final static Color outlineColor = Color.white;
	private final static float TWO_DIV_SQRT_3 = (float) (2 / Math.sqrt(3));
	private final static int SQRT_3_DIV_2 = (int) (Math.sqrt(3) / 2);
	
	private float animationCounter;
	
	public Renderer(Game game) {
		this.game = game;
		tiles = new ArrayList<>();
		//Add all the tiles to the list
		for (int i = 0; i < game.getTiles().length; i++) {
			for (int j = 0; j < game.getTiles()[0].length; j++) {
				tiles.add(game.getTiles()[i][j]);
			}
		}
		animationCounter = 0;
	}
	
	/**
	 * Renders all the hexagons and their borders
	 * @param g
	 */
	public void render(Graphics g, int width, int height) {

		g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        
		//Maybe use this to render faster?
		//BufferedImage img = new BufferedImage(game.getMainFrame().getDisplay().getWidth(), game.getMainFrame().getDisplay().getHeight(), BufferedImage.TYPE_INT_RGB);
		
		//The boundaries halfway through the window on screen
		int halfWidth = width / 2;
		int halfHeight = height / 2;
		
		//Update the positions of each point in relation to the camera's position and zoom
		game.getTileMap().getPoints().parallelStream().forEach((Point p) -> {
			p.update(game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZoom(), halfWidth, halfHeight);
		});
		
		//Calculate the minimum and maximum visible x and y values depending on the camera position and zoom in advance so we can reuse it without calculating it for each point
		float minX = game.getCamera().getX() - (halfWidth / game.getCamera().getZoom());
		float maxX = game.getCamera().getX() + (halfWidth / game.getCamera().getZoom());
		float minY = game.getCamera().getY() - (halfHeight / game.getCamera().getZoom());
		float maxY = game.getCamera().getY() + (halfHeight / game.getCamera().getZoom());
		
		//Figure out which tiles are visible
		visibleTiles = tiles.parallelStream().filter((Tile t) -> {
			return t.isVisible(minX, maxX, minY, maxY);
		}).collect(Collectors.toList());
		
		//Update the x and y values stored in the hexagons {@code Tile}s
		visibleTiles.parallelStream().forEach((Tile t) -> {
			t.updatePoints();
		});
		
		//Draw each {@code Tile}
		visibleTiles.forEach((Tile t) -> {
			drawTile(g, t);
			//drawHex(img, t.getColor(), t.getXPoints()[0], t.getYPoints()[0], hexwidth, hexheight);
		});
		//Draw each (@code Tile}'s outline
//		g.setColor(outlineColor);
//		visibleTiles.parallelStream().forEach((Tile t) -> {
//			drawOutline(img, t);
//		});

		shadeSelectedTile(g, minX, maxX, minY, maxY);
	}
	
	/**
	 * Draws an individual {@code Tile} onto a given {@code Graphics} object
	 **/
	private void drawTile(Graphics g, Tile t) {
		g.setColor(t.getColor());
		g.fillPolygon(t.getXPoints(), t.getYPoints(), 6);
	}
	
	private void shadeSelectedTile(Graphics g, float minX, float maxX, float minY, float maxY) {
		Tile st = game.getSelectedTile();
		if (st != null && st.isVisible(minX, maxX, minY, maxY)) {
			int red = (int) ((st.getColor().getRed() * (2 * Math.abs(0.5f - animationCounter)) + (255 * (1 - (2 * Math.abs(0.5f - animationCounter))))));
			if (red > 255) {
				red = 255;
			}
			int green = (int) ((st.getColor().getGreen() * (2 * Math.abs(0.5f - animationCounter)) + (255 * (1 - (2 * Math.abs(0.5f - animationCounter))))));
			if (green > 255) {
				green = 255;
			}
			int blue = (int) ((st.getColor().getBlue() * (2 * Math.abs(0.5f - animationCounter)) + (255 * (1 - (2 * Math.abs(0.5f - animationCounter))))));
			if (blue > 255) {
				blue = 255;
			}
			g.setColor(new Color(red, green, blue));
			g.fillPolygon(st.getXPoints(), st.getYPoints(), 6);
			g.setColor(Color.white);
			g.drawPolygon(st.getXPoints(), st.getYPoints(), 6);
		}
		animationCounter += 0.01f;
		if (animationCounter > 1f) {
			animationCounter = 0f;
		}
	}
	
	//Potential to draw hexes faster? (probably not)
//	public void drawHex(BufferedImage img, Color tileColor, int x, int y, int sidelength, int hexheight) {
//		long time = System.nanoTime();
//		if (y > img.getHeight()) {
//			return;
//		}
//		if (x > img.getWidth()) {
//			return;
//		}
//		int upperYBound = y + hexheight;
//		int crosspoint = y + (hexheight / 2);
//		int color = tileColor.getRGB();
//		if (y < 0) {
//			y = 0;
//		}
//		if (upperYBound >= img.getHeight()) {
//			upperYBound = img.getHeight() - 1;
//		}
//		for(int i = y; i <= upperYBound; i++) {
//			int halfExtraWidth = ((int) ((i < crosspoint) ? (i - y) * TWO_DIV_SQRT_3 : (sidelength * 2) - ((i - y) * TWO_DIV_SQRT_3))) / 2;
//			int upperXBound = x + sidelength + halfExtraWidth;
//			int initJ = x - halfExtraWidth;
//			if (initJ < 0) {
//				initJ = 0;
//			}
//			if (upperXBound >= img.getWidth()) {
//				upperXBound = img.getWidth() - 1;
//			}
//			for (int j = initJ; j <= upperXBound; j++) {
//				img.setRGB(j, i, color);
//			}
//		}
//		time = System.nanoTime() - time;
//		System.out.println(time);
//	}
	
	/**
	 * Draws part of an individual {@code Tile}'s outline onto a given {@code Graphics} object.
	 * Doesn't draw the entire outline to save time, as the tiles are adjacent.
	 **/
//	private void drawOutline(BufferedImage img, Tile tile) {
//		for (int i = 0; i < 6; i++) {
//			g.fillRect(tile.getXPoints()[i], tile.getYPoints()[i], 1, 1);
//		}
//		g.drawLine(tile.getXPoints()[0], tile.getYPoints()[0], tile.getXPoints()[1], tile.getYPoints()[1]);
//		g.drawLine(tile.getXPoints()[1], tile.getYPoints()[1], tile.getXPoints()[2], tile.getYPoints()[2]);
//		g.drawLine(tile.getXPoints()[2], tile.getYPoints()[2], tile.getXPoints()[3], tile.getYPoints()[3]);
		//g.drawPolygon(tile.getXPoints(), tile.getYPoints(), 6);
//	}

}
