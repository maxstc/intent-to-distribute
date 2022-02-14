package display;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import engine.Game;
import gameObject.Tile;

public class Renderer {
	private Game game;
	private List<Tile> tiles;
	private List<Tile> visibleTiles;
	private final static Color outlineColor = Color.black;
	
	public Renderer(Game game) {
		this.game = game;
		tiles = new ArrayList<>();
		for (int i = 0; i < game.getTiles().length; i++) {
			for (int j = 0; j < game.getTiles()[0].length; j++) {
				tiles.add(game.getTiles()[i][j]);
			}
		}
	}
	
	public void render(Graphics g) {
		float minX = game.getCamera().getX() - ((game.getMainFrame().getDisplay().getWidth() / 2) / (game.getCamera().getZoom()));
		float maxX = game.getCamera().getX() + ((game.getMainFrame().getDisplay().getWidth() / 2) / (game.getCamera().getZoom()));
		float minY = game.getCamera().getY() - ((game.getMainFrame().getDisplay().getHeight() / 2) / (game.getCamera().getZoom()));
		float maxY = game.getCamera().getY() + ((game.getMainFrame().getDisplay().getHeight() / 2) / (game.getCamera().getZoom()));
		
		int halfWidth = game.getMainFrame().getDisplay().getWidth() / 2;
		int halfHeight = game.getMainFrame().getDisplay().getHeight() / 2;
		visibleTiles = tiles.parallelStream().filter((Tile t) -> {
			return t.isVisible(minX, maxX, minY, maxY);
		}).collect(Collectors.toList());
		visibleTiles.parallelStream().forEach((Tile t) -> {
			t.updatePoints(halfWidth, halfHeight, game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZoom());
		});
		visibleTiles.forEach((Tile t) -> {
			drawTile(g, t);
		});
		g.setColor(outlineColor);
		visibleTiles.parallelStream().forEach((Tile t) -> {
			drawOutline(g, t);
		});
	}
	
	private void drawTile(Graphics g, Tile tile) {
		g.setColor(tile.getColor());
		g.fillPolygon(tile.getXPoints(), tile.getYPoints(), 6);
		//g.setColor(outlineColor);
		//g.drawPolygon(tile.getXPoints(), tile.getYPoints(), 6);
	}
	
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
