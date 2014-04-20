package main;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class GUI{
	
	/*
	 * Attributes
	 */
	private JFrame frame;
	private Container container;
	private BufferStrategy bufferStrategy;
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Clonaria");
        container = frame.getContentPane();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFont(new Font("Arial", Font.PLAIN, 20));
        frame.setBackground(Color.WHITE);
        frame.setForeground(Color.BLACK);
		frame.setFocusTraversalKeysEnabled(false); //Prevents tabbing to different areas.
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        //Create the buffer strategy (using a double buffer).
		frame.createBufferStrategy(2);
		bufferStrategy = frame.getBufferStrategy();
    }
    
    public JFrame getFrame(){
    	return frame;
    }
    
    /**
     * Returns a Graphics2D object centered on the drawable area.
     */
	public Graphics2D getGraphics(){
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		int borderThickness = (frame.getWidth() - container.getWidth()) / 2;
		int topThickness = frame.getHeight() - container.getHeight() - borderThickness;
		graphics.translate(borderThickness, topThickness);
		return graphics;
	}
	
    /**
     * Returns a Rectangle representing the drawable area in the screen's pixel coordinates.
     */
	public Rectangle getDrawableRect(){
		int borderThickness = (frame.getWidth() - container.getWidth()) / 2;
		int topThickness = frame.getHeight() - container.getHeight() - borderThickness;
		Rectangle rect = frame.getBounds();
		rect.translate(borderThickness, topThickness);
		rect.width -= borderThickness * 2;
		rect.height -= borderThickness + topThickness;
		return rect;
	}
	
    /**
     * Converts window coordinates to drawable area coordinates by subtracting the window border sizes.
     */
	public Point windowToDrawable(Point window){
		if(window == null) return null;
		int borderThickness = (frame.getWidth() - container.getWidth()) / 2;
		int topThickness = frame.getHeight() - container.getHeight() - borderThickness;
		return new Point(window.x - borderThickness, window.y - topThickness);
	}
	
    /**
     * Returns the mouse position within the drawable area.
     */
	public Point getMousePosition(){
		return windowToDrawable(frame.getMousePosition());
	}
	
	/**
	 * Get the width of the drawable area.
	 */
	public int getWidth(){
		return frame.getContentPane().getWidth();
	}
	
	/**
	 * Get the height of the drawable area.
	 */
	public int getHeight(){
		return frame.getContentPane().getHeight();
	}
	
	/**
	 * Update the display.
	 */
	public void update(){
		if(frame != null){
			BufferStrategy bufferStrategy = frame.getBufferStrategy();
			if(!bufferStrategy.contentsLost()){
				bufferStrategy.show();
			}else{
				Core.log(String.format("Warning: Frame %s contents lost", Core.get().totalFrames));
			}
		}else{
			Core.log(String.format("Warning: Frame %s is null", Core.get().totalFrames));
		}
	}
}
