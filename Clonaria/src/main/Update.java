package main;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.structs.Vector;

public class Update{
	public void update(){
		Entity player = Core.get().player;
		
		//Handle player movement.
		player.updateAgainstBlocks(0, player.getVelocity());
		if(KeyboardMouseListener.isPressed(KeyEvent.VK_A)) player.left();
		if(KeyboardMouseListener.isPressed(KeyEvent.VK_D)) player.right();
		if(KeyboardMouseListener.isPressed(KeyEvent.VK_SPACE)) player.jump();
		player.applyGravity(1);
		player.applyAirResistance(1);
		player.move(1);

		player.updateAgainstBlockDown(0, player.getVelocity());
		
		//Handle block breaking.
		if(KeyboardMouseListener.isPressed(MouseEvent.MOUSE_PRESSED)){
			Point p = Core.get().gui.getMousePosition();
			Vector block = Core.get().pixelsToBlocks(new Vector(p.x, p.y));
			if(block.distance(player.getLocation()) < 6){
				switch (((MouseEvent) KeyboardMouseListener.getEvent(MouseEvent.MOUSE_PRESSED)).getButton()){
					case 1: /* Left click */	Core.get().world.setBlockAt((int) block.x, (int) block.y, 1, "air"); break;
					case 3: /* Right click */	Core.get().world.placeBlockAt((int) block.x, (int) block.y, 1, "torch"); break;
				}
			}
		}
	}
}
