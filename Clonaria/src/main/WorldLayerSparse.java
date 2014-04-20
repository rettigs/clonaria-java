package main;

import java.util.HashMap;
import java.util.Map;

import main.structs.IntVector;

public class WorldLayerSparse implements WorldLayer{
	
	/*
	 * Attributes
	 */
	Map<IntVector, BlockModel> blockModels;
	
	public WorldLayerSparse(){
		this.blockModels = new HashMap<IntVector, BlockModel>();
	}
	
	public BlockModel getBlockAt(int x, int y){
		return blockModels.get(new IntVector(x, y));
	}
	
	public boolean setBlockAt(int x, int y, BlockModel tileModel){
		blockModels.put(new IntVector(x, y), tileModel);
		return true;
	}
	
	public boolean isEmptyAt(int x, int y){
		return getBlockAt(x, y) == null;
	}
	
	public boolean isSolidAt(int x, int y){
		if(isEmptyAt(x, y)) return false;
		else return blockModels.get(new IntVector(x, y)).getSolid();
	}

	public void load(String path, Map<Integer, BlockModel> keymap){
		
	}
	
	public void write(String path, Map<BlockModel, Integer> keymap){
		
	}
}