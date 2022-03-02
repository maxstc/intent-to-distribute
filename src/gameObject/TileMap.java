package gameObject;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the {@code Tile}s and methods dealing with their manipulation and creation
 */
public class TileMap {

	private Tile[][] tiles;
	private Set<Point> points;
	
	public TileMap(int width, int length) {
		tiles = new Tile[width][length];
		points = new HashSet<>();
		initializeTiles();
		//procedurally generate the map
		splitTiles(30);
		splitTiles(8);
		smooth(3);
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Creates the tiles of the map
	 */
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
	
	public Set<Point> getPoints() {
		return points;
	}
	
//	public void updatePoints() {
//		int halfWidth = game.getMainFrame().getDisplay().getWidth() / 2;
//		int halfHeight = game.getMainFrame().getDisplay().getHeight() / 2;
//		for (Point p : points) {
//			p.update(game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZoom(), halfWidth, halfHeight);
//		}
//	}
	
	/**
	 * Creates a new point given coordinates
	 * @param x
	 * @param y
	 * @return
	 */
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
	
	/**
	 * Calls {@code smooth()} on each tile, then resets their color and tileDataAlpha
	 * 
	 * Here, {@code reset()} sets the values so that each tile can be modified depending on their neighbors original tileDataAlpha. For example,
	 * if a neighbor has {@code smooth()} called and its tileDataAlpha was changed, if any of its neighbors called {@code smooth()}, they would
	 * read the first neighbor's tileDataAlpha as the changed one, resulting in odd effects that depend on which order {@code smooth()} is called.
	 * @param n the number of times to smooth
	 */
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
	
	/**
	 * Splits the tiles into n chunks and modifies the tileDataAlpha of each tile in each chunk by the same random number
	 * @param n
	 */
	public void splitTiles(int n) {
		
		Set<Tile>[] plates = new HashSet[n]; //an array containing each chunk (plate)
		Set<Tile> assignedTiles = new HashSet<>(); //keep track of tiles that have already been assigned to avoid overlap
		Set<Tile> disabledTiles = new HashSet<>(); //keep track of tiles whose neighbors are all already assigned to speed up the algorithm
		int numAssigned = 0; //keep track of the number of tiles assigned so we know when we're finished
		int numTiles = tiles.length * tiles[0].length;
		
		//initialize plates with unique source tiles
		for (int i = 0; i < n; i++) { //for each plate
			plates[i] = new HashSet<Tile>(); //initialize the plate
			Tile sourceTile = tiles[(int)(Math.random() * getTiles().length)][(int)(Math.random() * getTiles()[0].length)]; //try to set the source tile
			while (assignedTiles.contains(sourceTile)) { //keep trying to assign it if its already assigned
				sourceTile = tiles[(int)(Math.random() * getTiles().length)][(int)(Math.random() * getTiles()[0].length)];
			}
			plates[i].add(sourceTile);
			assignedTiles.add(sourceTile);
			numAssigned++;
		}
		
		while (numAssigned < numTiles) { //continue until we run out of tiles to assign
			for (int i = 0; i < plates.length; i++) { //for each plate
				for (Tile t : plates[i]) { //for each tile in this plate
					if (!disabledTiles.contains(t)) { //if the tile isnt disabled
						Set<Tile> neighbors = t.getNeighbors(); //get the tile's neighbors
						for (Tile s : neighbors) { //for each neighbor
							if (!assignedTiles.contains(s)) { //if it isn't already assigned
								plates[i].add(s); //add it to the plate
								assignedTiles.add(s); //add it to the list of assigned tiles
								numAssigned++; //increment the number of total assigned tiles
							}
						}
						disabledTiles.add(t); //disable the tile who's neighbors are all now assigned
					}
				}
			}
		}
		
		for (int i = 0; i < plates.length; i++) { //for each plate
			float rand = (float) Math.random(); //generate a random number
			for (Tile t : plates[i]) { //for each tile in that plate
				t.tileDataBeta = rand * 0.3f + t.tileDataBeta * 0.7f; //modify the tile's data by that random number
				t.reset(); //reset the tileDataAlpha and color of that tile
			}
		}
		
	}
	
}
