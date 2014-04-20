package main;

import java.awt.image.BufferedImage;

public class Item{
	
	/*
	 * Attributes
	 */
	public static Item[] defs = new Item[100];
	
	int id;
	String name;
	BufferedImage image;
	
	public Item(int id, String name, BufferedImage image){
		this.id = id;
		this.name = name;
		this.image = image;
	}	
}
