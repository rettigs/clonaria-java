package main.structs;

public class IntVector implements Cloneable{
	
	/*
	 * Attributes
	 */
	public int x;
	public int y;
	
	public IntVector(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the distance between self and the given vector.
	 */
	public double distance(IntVector v){
		return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
	}
	
	/**
	 * Duplicates this vector, adds the given values to it, and returns it.
	 */
	public IntVector plus(int x, int y){
		return new IntVector(this.x + x, this.y + y);
	}
	
	/**
	 * Duplicates this vector and returns it.
	 */
	public Vector clone(){
		return new Vector(this.x, this.y);
	}
}
