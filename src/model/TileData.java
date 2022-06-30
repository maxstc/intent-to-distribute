package model;

import java.util.HashSet;
import java.util.Set;

import gameObject.Tile;

/**
 * This class contains data dealing with the game model
 */
public class TileData {
	
	private static float SEA_LEVEL = 0.4f;
	private static float POP_COEFF = 1f / (1f - SEA_LEVEL);
	private static float COAST_BONUS = 0.7f;

	private Tile parent;
	private float height;
	private float temp;
	private float tempVar;
	//private float rain;
	private boolean isLand;
	private float pop;
	private float civ;
	
	private Settlement settlement;
	
	public TileData(Tile parent) {
		this.parent = parent;
		height = (float) Math.random();
		temp = 0f;
		tempVar = 0f;
		isLand = false;
		settlement = null;
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
	
	public float getPop() {
		return pop;
	}
	
	public void setTempVar(float tempVar) {
		this.tempVar = tempVar;
	}
	
	public boolean getIsLand() {
		return isLand;
	}
	
	public float getCiv() {
		return civ;
	}
	
	public void calcTemp() {
		float latitude = parent.getY() / 100f;
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
		//System.out.println(x);
	}
	
	public void calcPop() {
		if (height < SEA_LEVEL) {
			pop = 0f;
		}
		else {
			pop = (1f - height) * POP_COEFF;
		}
	}
	
	public void calcIsLand() {
		isLand = height > 0.4f;
	}
	
	public void calcCiv() {
		if (!isLand) {
			civ = 0f;
			return;
		}
		float total = 0f;
		boolean isCoastal = false;
		Set<Tile> n = parent.getNeighbors();
		Set<Tile> neighbors = new HashSet<>();
		for (Tile t : n) {
			if (!t.getTileData().getIsLand()) {
				isCoastal = true;
			}
			neighbors.addAll(t.getNeighbors());
		}
		int num = 0;
		for (Tile t : neighbors) {
			total += t.getTileData().getPop();
			num++;
		}
		civ = total / num;
		if (isCoastal)  {
			//civ = (1f - civ) * COAST_BONUS + civ;
			civ = (3f * civ) - (3 * civ * civ) + (civ * civ * civ);
			//civ = (2f * civ) - (civ * civ);
		}
	}
	
}
