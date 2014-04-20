package main;

import java.util.Map;

public interface WorldLayer{
	public BlockModel getBlockAt(int x, int y);
	public boolean setBlockAt(int x, int y, BlockModel tileModel);
	public boolean isEmptyAt(int x, int y);
	public boolean isSolidAt(int x, int y);
	public void load(String path, Map<Integer, BlockModel> keymap) throws Exception;
	public void write(String path, Map<BlockModel, Integer> keymap) throws Exception;
}