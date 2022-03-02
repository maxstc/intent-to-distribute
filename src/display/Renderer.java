package display;

import java.awt.Color;
import java.awt.Graphics;
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
	private final static Color outlineColor = Color.black;
	
	public Renderer(Game game) {
		this.game = game;
		tiles = new ArrayList<>();
		//Add all the tiles to the list
		for (int i = 0; i < game.getTiles().length; i++) {
			for (int j = 0; j < game.getTiles()[0].length; j++) {
				tiles.add(game.getTiles()[i][j]);
			}
		}
	}
	
	/**
	 * Renders all the hexagons and their borders
	 * @param g
	 */
	public void render(Graphics g) {
		
		//The boundaries halfway through the window on screen
		int halfWidth = game.getMainFrame().getDisplay().getWidth() / 2;
		int halfHeight = game.getMainFrame().getDisplay().getHeight() / 2;
		
		//Update the positions of each point in relation to the camera's position and zoom
		game.getTileMap().getPoints().parallelStream().forEach((Point p) -> {
			p.update(game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZoom(), halfWidth, halfHeight);
		});
		
		//Calculate the minimum and maximum visible x and y values depending on the camera position and zoom in advance so we can reuse it without calculating it for each point
		float minX = game.getCamera().getX() - ((game.getMainFrame().getDisplay().getWidth() / 2) / (game.getCamera().getZoom()));
		float maxX = game.getCamera().getX() + ((game.getMainFrame().getDisplay().getWidth() / 2) / (game.getCamera().getZoom()));
		float minY = game.getCamera().getY() - ((game.getMainFrame().getDisplay().getHeight() / 2) / (game.getCamera().getZoom()));
		float maxY = game.getCamera().getY() + ((game.getMainFrame().getDisplay().getHeight() / 2) / (game.getCamera().getZoom()));
		
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
		});
		//Draw each (@code Tile}'s outline
		g.setColor(outlineColor);
		visibleTiles.parallelStream().forEach((Tile t) -> {
			drawOutline(g, t);
		});
	}
	
	/**
	 * Draws an individual {@code Tile} onto a given {@code Graphics} object
	 **/
	private void drawTile(Graphics g, Tile tile) {
		g.setColor(tile.getColor());
		g.fillPolygon(tile.getXPoints(), tile.getYPoints(), 6);
		//g.setColor(outlineColor);
		//g.drawPolygon(tile.getXPoints(), tile.getYPoints(), 6);
	}
	
	/**
	 * Draws part of an individual {@code Tile}'s outline onto a given {@code Graphics} object.
	 * Doesn't draw the entire outline to save time, as the tiles are adjacent.
	 **/
	private void drawOutline(Graphics g, Tile tile) {
//		for (int i = 0; i < 6; i++) {
//			g.fillRect(tile.getXPoints()[i], tile.getYPoints()[i], 1, 1);
//		}
		g.drawLine(tile.getXPoints()[0], tile.getYPoints()[0], tile.getXPoints()[1], tile.getYPoints()[1]);
		g.drawLine(tile.getXPoints()[1], tile.getYPoints()[1], tile.getXPoints()[2], tile.getYPoints()[2]);
		g.drawLine(tile.getXPoints()[2], tile.getYPoints()[2], tile.getXPoints()[3], tile.getYPoints()[3]);
		//g.drawPolygon(tile.getXPoints(), tile.getYPoints(), 6);
	}

}
