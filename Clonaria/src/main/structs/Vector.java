package main.structs;

public class Vector implements Cloneable{
	
	/*
	 * Attributes
	 */
	public double x;
	public double y;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the distance between self and the given vector.
	 */
	public double distance(Vector v){
		return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
	}
	
	/**
	 * Duplicates this vector, adds the given values to it, and returns it.
	 */
	public Vector plus(double x, double y){
		return new Vector(this.x + x, this.y + y);
	}
	
	/**
	 * Duplicates this vector and returns it.
	 */
	public Vector clone(){
		return new Vector(this.x, this.y);
	}
}
