package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import main.structs.Vector;

public class World{
	
	/*
	 * Attributes
	 */
	private String name;
	private int width;
	private int height;
	private WorldLayer[] layers = new WorldLayer[5];
	
	Core core;
	
	public World(String name, int width, int height){
		this.setName(name);
		this.width = width;
		this.height = height;
		
		layers[0] = new WorldLayerSparse();				// Background layer
		layers[1] = new WorldLayerDense(width, height);	// Midground layer
		layers[2] = new WorldLayerSparse();				// Foreground layer
		layers[3] = new WorldLayerSparse();				// Actuator layer
		layers[4] = new WorldLayerSparse();				// Wire layer
		
		core = Core.get();
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public boolean isValidCoords(int x, int y, int layer){
		return x >= 0 && y >= 0 && x < getWidth() && y < getHeight() && layer >= 0 && layer < layers.length;
	}
	
	public BlockModel getBlockAt(int x, int y, int layer){
		if(isValidCoords(x, y, layer)){
			return layers[layer].getBlockAt(x, y);
		}else{
			return null;
		}
	}
	
	/**
	 * Places the block at the given location only if it is allowed to be placed there according to its attachment mask and those of its neighbors.
	 */
	public boolean placeBlockAt(int x, int y, int layer, String blockModel){
		if(!this.isEmptyAt(x, y, layer)) return false;
		
		boolean placeable = true;
		BlockModel block = core.blockModels.get(blockModel);
		BlockModel otherBlock;
		
		Core.logf("Attempting to place %s at %d, %d", blockModel, x, y);
		Core.log(block.getIcanattachto());

//		if(block.iCanAttachFront()){
//			otherBlock = this.getBlockAt(x, y, layer+1);
//			if(otherBlock != null && otherBlock.theyCanAttachBack()) placeable = true;
//			Core.log("yay");
//		}else if(block.iCanAttachBack()){
//			otherBlock = this.getBlockAt(x, y, layer-1);
//			if(otherBlock != null && otherBlock.theyCanAttachFront()) placeable = true;
//			Core.log("yay");
//		}else if(block.iCanAttachTop()){
//			otherBlock = this.getBlockAt(x, y+1, layer);
//			if(otherBlock != null && otherBlock.theyCanAttachBottom()) placeable = true;
//			Core.log("yay");
//		}else if(block.iCanAttachRight()){
//			otherBlock = this.getBlockAt(x+1, y, layer);
//			if(otherBlock != null && otherBlock.theyCanAttachLeft()) placeable = true;
//			Core.log("yay");
//		}else if(block.iCanAttachBottom()){
//			otherBlock = this.getBlockAt(x, y-1, layer);
//			if(otherBlock != null && otherBlock.theyCanAttachTop()) placeable = true;
//			Core.log("yay");
//		}else if(block.iCanAttachLeft()){
//			otherBlock = this.getBlockAt(x-1, y, layer);
//			if(otherBlock != null && otherBlock.theyCanAttachRight()) placeable = true;
//			Core.log("yay");
//		}
		
		if(placeable) return setBlockAt(x, y, layer, block);
		else return false;
	}
	
	/**
	 * Sets the block at the given location regardless of placement/breaking rules.
	 */
	public boolean setBlockAt(int x, int y, int layer, String blockModel){
		return setBlockAt(x, y, layer, core.blockModels.get(blockModel));
	}
	
	public boolean setBlockAt(int x, int y, int layer, BlockModel blockModel){
		if(isValidCoords(x, y, layer)){
			layers[layer].setBlockAt(x, y, blockModel);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isEmptyAt(int x, int y, int layer){
		if(isValidCoords(x, y, layer)){
			return layers[layer].isEmptyAt(x, y);
		}else{
			return false;
		}
	}
	
	public boolean isSolidAt(int x, int y, int layer){
		if(isValidCoords(x, y, layer)){
			return layers[layer].isSolidAt(x, y);
		}else{
			return false;
		}
	}
	
	public void generate(){
		int w = getWidth();
		int h = getHeight();
		
		for(int x = 0; x < w; x++){
			for(int y = 0; y < h; y++){
				setBlockAt(x, y, 1, "air");
			}
		}
		
		for(int x = 0; x < w; x++){
			for(int y = 0; y < h/2 + Math.random() * 10; y++){
				setBlockAt(x, y, 1, "dirt");
			}
		}
		
		for(int i = 0; i < 10000; i++){
			double radius = Math.random() * 20;
			Set<Vector> blocks = Util.circle(Math.random() * w, Math.random() * h, radius);
			for(Vector block : blocks){
				if(!this.isEmptyAt((int) block.x, (int) block.y, 1)){
					this.setBlockAt((int) block.x, (int) block.y, 1, "dirt");
				}
			}
		}
		
		for(int i = 0; i < 5000; i++){
			double radius = Math.random() * 20;
			Set<Vector> blocks = Util.circle(Math.random() * w, Math.random() * h, radius);
			for(Vector block : blocks){
				if(!this.isEmptyAt((int) block.x, (int) block.y, 1)){
					this.setBlockAt((int) block.x, (int) block.y, 1, "sand");
				}
			}
		}
		
		for(int i = 0; i < 10000; i++){
			double radius = Math.random() * 8;
			Set<Vector> blocks = Util.circle(Math.random() * w, Math.random() * h, radius);
			for(Vector block : blocks){
				if(!this.isEmptyAt((int) block.x, (int) block.y, 1)){
					this.setBlockAt((int) block.x, (int) block.y, 1, "gravel");
				}
			}
		}
	}	
	/**
	 * Load the world the disk.  Throws an exception if not successful.
	 */
	public void load() throws Exception{
		String path = "worlds/"+name;
		
		File key = new File(path+"/key.csv");
		Scanner scanner = new Scanner(key);
		
		Map<Integer, BlockModel> keymap = new HashMap<Integer, BlockModel>();
		while(scanner.hasNextLine()){
			String[] line = scanner.nextLine().split(",");
			int id = Integer.parseInt(line[0]);
			BlockModel model = Core.get().blockModels.get(line[1]);
			keymap.put(id, model);
		}

		scanner.close();
		
		int i = 0;
		for(WorldLayer layer : layers){
			layer.load(path+"/"+i, keymap);
		}
	}
	
	/**
	 * Write the world the disk.  Throws an exception if not successful.
	 */
	public void write() throws Exception{
		String path = "worlds/"+name;
		
		File dir = new File(path);
		if(!dir.mkdirs()) Core._exit();
		
		File key = new File(path+"/key.csv");
		PrintWriter writer = new PrintWriter(key);
		
		Map<BlockModel, Integer> keymap = new HashMap<BlockModel, Integer>();
		int i = 0;
		for(Entry<String, BlockModel> entry : core.blockModels.entrySet()){
			writer.println(i+","+entry.getKey());
			keymap.put(entry.getValue(), i);
			i++;
		}
		
		writer.flush();
		writer.close();
		
		i = 0;
		for(WorldLayer layer : layers){
			layer.write(path+"/"+i, keymap);
		}
	}
}