package test;

import main.structs.Vector;

public class TestVector{
	public static void main(String[] args){
		Vector a = new Vector(9793, 4554);
		Vector b = new Vector(-300, 888);
		System.out.println("Distance: " + a.distance(b));
	}
}
