package gen;

import gameObject.TileMap;

public abstract class WorldGenerator {

	private int mapWidth;
	private int mapHeight;
	private int numSplitsAlpha;
	private int numSplitsBeta;
	private int numSplitsGamma;
	private int numSmooths;
	
	public WorldGenerator() {
		
	}
	
	public TileMap generate() {
		return null;
	}
	
}
