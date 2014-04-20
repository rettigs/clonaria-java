package test;

import java.util.Set;

import main.Util;
import main.structs.Vector;

public class TestCircle{
	public static void main(String[] args){
		double radius = Math.random() * 10;
		Set<Vector> blocks = Util.circle(Math.random() * 10, Math.random() * 10, radius);
		System.out.printf("radius: %f\n", radius);
		for(Vector block : blocks){
			System.out.printf("%f, %f\n", block.x, block.y);
		}
	}
}
