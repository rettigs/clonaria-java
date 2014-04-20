package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import main.structs.Vector;

public class Draw{
	
	/*
	 * Attributes
	 */
	private GUI gui;
	Entity player;
	
	public Draw(GUI gui){
		this.gui = gui;
		player = Core.get().player;
	}

	/**
	 * Draws a frame of gameplay.
	 */
	public void draw(int fps, long totalFrames){
		Graphics2D graphics = gui.getGraphics();
		
		// Draw the background
		graphics.setColor(Color.CYAN);
		graphics.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Draw the world around the player
		int blocksOutHorizontal = gui.getWidth() / 2 / Core.PIXELS_PER_BLOCK + 1;
		int blocksOutVertical = gui.getHeight() / 2 / Core.PIXELS_PER_BLOCK + 2;
		for(int y = (int) player.getLocationY() - blocksOutVertical; y < player.getLocationY() + blocksOutVertical; y++){
			for(int x = (int) player.getLocationX() - blocksOutHorizontal; x < player.getLocationX() + blocksOutHorizontal; x++){
				BlockModel blockModel = Core.get().world.getBlockAt(x, y, 1);
				if(blockModel != null) drawImageFrom(Core.get().blocksToPixels(new Vector(x, y)), blockModel.getTexture());
			}
		}
		
		// Draw debug info
		graphics.setColor(Color.YELLOW);
		graphics.drawString("Total frames: " + totalFrames, 0, 20);
		graphics.drawString("FPS: " + fps, 0, 40);
		graphics.drawString("player.location.x: " + player.getLocationX(), 0, 60);
		graphics.drawString("player.location.y: " + player.getLocationY(), 0, 80);
		graphics.drawString("player.againstBlockUp: " + player.againstBlockUp, 0, 100);
		graphics.drawString("player.againstBlockDown: " + player.againstBlockDown, 0, 120);
		graphics.drawString("player.againstBlockLeft: " + player.againstBlockLeft, 0, 140);
		graphics.drawString("player.againstBlockRight: " + player.againstBlockRight, 0, 160);
		
		// Draw the player
		drawImageFrom(Core.get().blocksToPixels(new Vector(player.getLocationX(), player.getLocationY())), new Vector(1, 2), player.getTexture());
		
		// Draw the player's hitbox
		graphics.setColor(Color.RED);
		graphics.drawPolygon(player.getHitbox(1, player.getVelocity()));
		
//		// Highlight blocks near the player
//		graphics.setColor(new Color(255, 0, 0, 128));
//		Set<Vector> nearbyBlocks = player.getNearbyBlocks(0, player.getVelocity());
//		for(Vector block : nearbyBlocks){
//			Vector pixelCoords = Core.get().blocksToPixels(block);
//			graphics.fillRect((int) pixelCoords.x, (int) pixelCoords.y, Core.PIXELS_PER_BLOCK, Core.PIXELS_PER_BLOCK);
//		}
		
		// Highlight blocks around the player
		graphics.setColor(new Color(255, 0, 0, 128));
		Set<Vector> nearbyBlocks = new HashSet<Vector>();
		nearbyBlocks.addAll(player.getBlocksUp(0, player.getVelocity()));
		nearbyBlocks.addAll(player.getBlocksDown(0, player.getVelocity()));
		nearbyBlocks.addAll(player.getBlocksLeft(0, player.getVelocity()));
		nearbyBlocks.addAll(player.getBlocksRight(0, player.getVelocity()));
		for(Vector block : nearbyBlocks){
			Vector pixelCoords = Core.get().blocksToPixels(block);
			graphics.fillRect((int) pixelCoords.x, (int) pixelCoords.y, Core.PIXELS_PER_BLOCK, Core.PIXELS_PER_BLOCK);
		}
		
		// Highlight the currently targeted block
		graphics.setColor(new Color(255, 255, 0, 128));
		Point p = Core.get().gui.getMousePosition();
		if(p != null){
			Vector block = Core.get().pixelsToBlocks(new Vector(p.x, p.y));
			Vector pixel = Core.get().blocksToPixels(new Vector((int) block.x, (int) block.y));
			graphics.fillRect((int) pixel.x, (int) pixel.y, Core.PIXELS_PER_BLOCK, Core.PIXELS_PER_BLOCK);
		}
		
		graphics.dispose();
		gui.update();
	}
	
	/**
	 * Draws the given image at the specified pixel coordinates.
	 */
	private void drawImageFrom(Vector coords, Image image){
		drawImageFrom(coords, new Vector(1, 1), image);
	}
	
	/**
	 * Draws the given image at the specified pixel coordinates.
	 */
	private void drawImageFrom(Vector coords, Vector size, Image image){
		gui.getGraphics().drawImage(image, (int) coords.x, (int) coords.y, (int) size.x * Core.PIXELS_PER_BLOCK, (int) size.y * Core.PIXELS_PER_BLOCK, gui.getFrame());
	}
}
