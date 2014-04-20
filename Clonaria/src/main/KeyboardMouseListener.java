package main;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

public class KeyboardMouseListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	/*
	 * Attributes
	 */
	static Map<Integer, InputEvent> buttons = new HashMap<Integer, InputEvent>();
	
	public void keyPressed(KeyEvent event){
		int button = event.getKeyCode();
		if(button == KeyEvent.VK_ESCAPE){
			Core.get().exit();
		}else{
			buttons.put(button, event);
		}
		event.consume(); //Consumes the key press so it can't be later used in system key combinations.
	}
	
	public void keyReleased(KeyEvent event){
		buttons.remove(event.getKeyCode());
		event.consume(); //Consumes the key press so it can't be later used in system key combinations.
	}
	
	public void keyTyped(KeyEvent event){
		event.consume(); //Consumes the key press so it can't be later used in system key combinations.
	}
	
	public static boolean isPressed(int button){
		return buttons.containsKey(button);
	}
	
	public static InputEvent getEvent(int button){
		return buttons.get(button);
	}
	
	public void mousePressed(MouseEvent e){
		if(isPressed(MouseEvent.MOUSE_PRESSED)) buttons.remove(MouseEvent.MOUSE_PRESSED);
		buttons.put(MouseEvent.MOUSE_PRESSED, e);
		e.consume();
	}
	
	public void mouseReleased(MouseEvent e){
		buttons.remove(MouseEvent.MOUSE_PRESSED);
		e.consume();
	}
	
	public void mouseClicked(MouseEvent e){
		
	}
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}
	
	public void mouseDragged(MouseEvent e){
		
	}
	
	public void mouseMoved(MouseEvent e){
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e){
		
	}
}
