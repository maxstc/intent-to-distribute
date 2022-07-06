package gameObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.Game;

/**
 * This class contains the {@code Tile}s and methods dealing with their manipulation and creation
 */
public class TileMap {

	private Tile[][] tiles;
	private Set<Point> points;
	
	private int mapMode;
	
	public TileMap(int width, int length) {
		tiles = new Tile[width][length];
		points = new HashSet<>();
		mapMode = -1;
		initializeTiles();

		//procedurally generate the map
		splitTiles(50);
		splitTiles(10);
		splitTiles(3);
		smooth(4);
		calcTemp();
		calcIsLand();
		calcRain();
		calcPop();
		calcCiv();
		calcSettlement();
		updateMapMode(0);
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
	}
	
	public void updateMapMode(int mapMode) {
		//System.out.println("mapmode" + mapMode);
		if (mapMode >= 0 && mapMode <= 4 && mapMode != this.mapMode) {
			//System.out.println("doing");
			this.mapMode = mapMode;
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tiles[i][j].resetColor(mapMode);
				}
			}
		}
	}
	
	public Set<Point> getPoints() {
		return points;
	}
	
	/**
	 * Creates a new point given coordinates
	 * @param x
	 * @param y
	 * @return
	 */
	public Point createNewPoint(float x, float y) {
		
		Point newPoint = new Point(x, y);
		
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
					tiles[i][j].getTileData().smooth();
				}
			}
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tiles[i][j].getTileData().reset();
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
				Set<Tile> newTiles = new HashSet<>(); //temp varaible to avoid concurrent modification
				for (Tile t : plates[i]) { //for each tile in this plate
					if (!disabledTiles.contains(t)) { //if the tile isnt disabled
						Set<Tile> neighbors = t.getNeighbors(); //get the tile's neighbors
						for (Tile s : neighbors) { //for each neighbor
							if (!assignedTiles.contains(s)) { //if it isn't already assigned
								newTiles.add(s); //add it to the plate
								assignedTiles.add(s); //add it to the list of assigned tiles
								numAssigned++; //increment the number of total assigned tiles
							}
						}
						disabledTiles.add(t); //disable the tile who's neighbors are all now assigned
					}
				}
				plates[i].addAll(newTiles);
			}
		}
		
		for (int i = 0; i < plates.length; i++) { //for each plate
			float rand = (float) Math.random(); //generate a random number
			for (Tile t : plates[i]) { //for each tile in that plate
				t.getTileData().setTempVar(rand * 0.3f + t.getTileData().getHeight() * 0.7f); //modify the tile's data by that random number
				t.getTileData().reset(); //reset the height and color of that tile
				
				//TODO separate the resetting to make the calcRain function
			}
		}
	}
	
	public void calcTemp() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].getTileData().calcTemp();
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
			return null;
		}
		return tiles[x][y];
	}
	
	public void calcIsLand() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].getTileData().calcIsLand();
			}
		}
	}
	
	public void calcRain() {
		
	}
	
	public void calcPop() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].getTileData().calcPop();
			}
		}
	}
	
	public void calcCiv() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].getTileData().calcCiv();
			}
		}
	}
	
	public void calcSettlement() {
		List<Tile> sortedTiles = new ArrayList<>(10000);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				sortedTiles.add(tiles[i][j]);
			}
		}
		sortedTiles.sort(new Comparator<Tile>() {

			@Override
			public int compare(Tile t1, Tile t2) {
				//System.out.println(t2.getTileData().getCiv() - t1.getTileData().getCiv());
				int value = (int) (100000f * (t2.getTileData().getCiv() - t1.getTileData().getCiv()));
				//System.out.println(value);
				return value;
			}
			
		});
		//minimum civ value for city
		boolean cont = true;
		while(cont) {
			Tile cityTile = sortedTiles.get(0);
			cityTile.getTileData().createSettlement();
			sortedTiles.remove(0);
			Set<Tile> neighbors = new HashSet<>();
			neighbors.add(cityTile);
			for (int i = 0; i < 3f / (cityTile.getTileData().getCiv() * cityTile.getTileData().getCiv()); i++) {
				Set<Tile> newNeighbors = new HashSet<>();
				for (Tile t : neighbors) {
					newNeighbors.addAll(t.getNeighbors());
				}
				neighbors.addAll(newNeighbors);
			}
			sortedTiles.removeAll(neighbors);
			
			if (sortedTiles.size() == 0) {
				cont = false;
			}
			else if (sortedTiles.get(0).getTileData().getCiv() < 0.1) {
				cont = false;
			}
			
		}
	}
	
}
