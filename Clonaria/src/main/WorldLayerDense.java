package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class WorldLayerDense implements WorldLayer{
	
	/*
	 * Attributes
	 */
	BlockModel[][] blockModels;
	
	public WorldLayerDense(int width, int height){
		this.blockModels = new BlockModel[width][height];
	}
	
	public BlockModel getBlockAt(int x, int y){
		return blockModels[x][y];
	}
	
	public boolean setBlockAt(int x, int y, BlockModel blockModel){
		blockModels[x][y] = blockModel;
		return true;
	}
	
	public boolean isEmptyAt(int x, int y){
		return getBlockAt(x, y) == Core.get().blockModels.get("air");
	}
	
	public boolean isSolidAt(int x, int y){
		return blockModels[x][y].getSolid();
	}
	
	public void load(String path, Map<Integer, BlockModel> keymap) throws Exception{
		File models = new File(path+".models.gz");
		FileInputStream filestream = new FileInputStream(models);
		GZIPInputStream gzipstream = new GZIPInputStream(filestream);
		DataInputStream datastream = new DataInputStream(gzipstream);

		boolean EOF = false;
		for(int y = 0; y < blockModels[1].length; y++){
			for(int x = 0; x < blockModels[0].length; x++){
				try{
					blockModels[x][y] = keymap.get(datastream.readInt());
				}catch(EOFException e){
					EOF = true;
				}
				if(EOF) break;
			}
			if(EOF) break;
		}

		datastream.close();
		gzipstream.close();
		filestream.close();
	}
	
	public void write(String path, Map<BlockModel, Integer> keymap) throws Exception{		
		File models = new File(path+".models.gz");
		FileOutputStream filestream = new FileOutputStream(models);
		GZIPOutputStream gzipstream = new GZIPOutputStream(filestream);
		DataOutputStream datastream = new DataOutputStream(gzipstream);

		for(int y = 0; y < blockModels[1].length; y++){
			for(int x = 0; x < blockModels[0].length; x++){
				datastream.writeInt(keymap.get(blockModels[x][y]));
			}
		}

		datastream.flush();
		gzipstream.flush();
		filestream.flush();
		
		datastream.close();
		gzipstream.close();
		filestream.close();
	}
}