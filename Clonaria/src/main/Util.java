package main;

import java.util.HashSet;
import java.util.Set;

import main.structs.Vector;

/**
 * Holds static utilities.
 */

public class Util{
	
	/**
	 * Returns true if the given value would round up, i.e. if it is closer to its ceil than its floor.
	 */
	public static boolean wouldRoundUp(double value){
		return value >= Math.floor(value) + 0.5;
	}
	
	public static Set<Vector> circle(double x, double y, double radius){
		Set<Vector> blocks = new HashSet<Vector>();
		
		for(double ix = x - radius; ix < x + radius; ix++){
			for(double iy = y - radius; iy < y + radius; iy++){
				if(Math.pow(ix - x, 2) + Math.pow(iy - y, 2) < Math.pow(radius, 2)){
					blocks.add(new Vector(ix, iy));
				}
			}
		}
		
		return blocks;
	}
}