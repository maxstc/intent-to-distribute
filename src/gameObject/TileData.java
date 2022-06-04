package gameObject;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains data dealing with the game model
 */
public class TileData {

	private Tile parent;
	private float height;
	private float temp;
	private float tempVar;
	private boolean isLand;
	
	public TileData(Tile parent) {
		this.parent = parent;
		height = (float) Math.random();
		temp = 0f;
		tempVar = 0f;
		isLand = false;
	}
	
	/**
	 * Finds the neighbors of this tile and sets this tile's tileDataAlpha to a weighted average of its and its neighbors (and their neighbors') tileDataAlpha
	 */
	public void smooth() {
		tempVar = 0f;
		Set<Tile> n = parent.getNeighbors();
		Set<Tile> neighbors = new HashSet<>();
		for (Tile t : n) {
			neighbors.addAll(t.getNeighbors());
		}
		int num = 0;
		for (Tile t : neighbors) {
			tempVar += t.getTileData().getHeight();
			num++;
		}
		tempVar /= num;
		tempVar = height * 0.3f + tempVar * 0.7f;
		tempVar = 0.5f * sigmoid(tempVar) + 0.5f * tempVar;
	}
	
	/**
	 * Performs a sigmoid-like function on {@code x}
	 * 
	 * It is x^2 from 0 to 0.5 and -2(x-1)^2+1 from 0.5 to 1
	 * @param x
	 * @return
	 */
	private static float sigmoid(float x) {
		if (x <= 0.5f) {
			return 2 * x * x;
		}
		return -2 * (x - 1) * (x - 1) + 1;
	}
	
	public void reset() {
		height = tempVar;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getTemp() {
		return temp;
	}
	
	public float getTempVar() {
		return tempVar;
	}
	
	public void setTempVar(float tempVar) {
		this.tempVar = tempVar;
	}
	
	public boolean getIsLand() {
		return isLand;
	}
	
	public void calcTemp() {
		float latitude = Math.abs(50 - parent.getY()) / 50f;
		float x = 1f - (height * height * height) - (latitude * latitude);
		if (x < 0f) {
			temp = 0f;
		}
		else if (x > 1f) {
			temp = 1f;
		}
		else {
			temp = x;
		}
		System.out.println(x);
	}
	
	public void calcIsLand() {
		isLand = height < 0.6f;
	}
	
}
