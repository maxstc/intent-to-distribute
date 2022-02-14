package gameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import engine.Game;

public class TileMap {

	private Tile[][] tiles;
	private Game game;
	private Set<Point> points;
	
	public TileMap(int width, int length, Game game) {
		tiles = new Tile[width][length];
		this.game = game;
		points = new HashSet<>();
		initializeTiles();
		splitTiles(30);
		smooth(3);
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	private void initializeTiles() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = new Tile(i, j, 1.0f, this);
			}
		}
		for (int k = 0; k < 0; k++) {
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tiles[i][j].smooth();
				}
			}
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tiles[i][j].reset();
				}
			}
		}
	}
	
	public void updatePoints() {
		int halfWidth = game.getMainFrame().getDisplay().getWidth() / 2;
		int halfHeight = game.getMainFrame().getDisplay().getHeight() / 2;
		for (Point p : points) {
			p.update(game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZoom(), halfWidth, halfHeight);
		}
	}
	
	public Point createNewPoint(float x, float y) {
		
		Point newPoint = new Point(x, y);
		
//		for (Point p : points) {
//			if (newPoint.getX() == p.getX() && newPoint.getY() == p.getY()) {
//				System.out.println("Duplicate point at" + p.getX() + ", " + p.getY());
//			}
//		}
		
		points.add(newPoint);
		return newPoint;
		
	}
	
	public void smooth(int n) {
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tiles[i][j].smooth();
				}
			}
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tiles[i][j].reset();
				}
			}
		}
	}
	
	public void splitTiles(int n) {
		
		Tile[] sources = new Tile[n];
		Set<Tile>[] plates = new HashSet[n];
		Set<Tile> assignedTiles = new HashSet<>();
		Set<Tile> disabledTiles = new HashSet<>();
		int numAssigned = 0;
		int numTiles = tiles.length * tiles[0].length;
		
		for (int i = 0; i < sources.length; i++) {
			plates[i] = new HashSet<Tile>();
			Tile sourceTile = tiles[(int)(Math.random() * getTiles().length)][(int)(Math.random() * getTiles()[0].length)];
			while (assignedTiles.contains(sourceTile)) {
				sourceTile = tiles[(int)(Math.random() * getTiles().length)][(int)(Math.random() * getTiles()[0].length)];
			}
			sources[i] = sourceTile;
			plates[i].add(sourceTile);
			assignedTiles.add(sourceTile);
			numAssigned++;
		}
		
		while (numAssigned < numTiles) {
			for (int i = 0; i < plates.length; i++) {
				Set<Tile> newTiles = new HashSet<>();
				for (Tile t : plates[i]) {
					if (!disabledTiles.contains(t)) {
						//do stuff
						Set<Tile> neighbors = t.getNeighbors();
						for (Tile s : neighbors) {
							if (!assignedTiles.contains(s)) {
								newTiles.add(s);
								assignedTiles.add(s);
								numAssigned++;
							}
						}
						disabledTiles.add(t);
					}
				}
				plates[i].addAll(newTiles);
			}
		}
		
		for (int i = 0; i < plates.length; i++) {
			float rand = (float) Math.random();
			for (Tile t : plates[i]) {
				t.tileDataBeta = rand * 0.3f + t.tileDataBeta * 0.7f;
				t.reset();
			}
		}
		
	}
	
}
