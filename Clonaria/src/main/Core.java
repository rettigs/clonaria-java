package main;
import java.awt.Graphics2D;
import java.awt.Window;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

import main.structs.Vector;

/**
 * Clonaria v2 fixes some things that were implemented poorly in v1:
 * 
 * - Game runs in windowed mode instead of fullscreen.
 * - Game is multithreaded and has separate tick and drawing loops, allowing the game to behave more consistently on different hardware and support varying framerates.
 * - Game loops are monitored by an external thread rather than being run in a try/finally loop (to improve performance).
 * - AI is double-buffered to allow consistent behaviour and easier multithreading.
 * - Game resources are loaded once instead of being loaded on every tick (oops!).
 * - Game has sound!
 * - All coordinates are in block increments instead of pixel coordinates for easier coding.
 * - Graphics are separated out into their own class.
 * - All entity movement is controlled through Command objects, which allow for easy AI control and such.
 * - There are no longer different subclasses of Entity for each type of entity physics.  Physics flags are set as attributes on the entity so their behaviour can be adjusted on the fly.
 */

public class Core{
	
	/*
	 * Attributes
	 */
	
	private static Core core; //Only one core will ever be instantiated, and there is a getter method for it get() so it can be accessed globally but never changed.
	public static boolean running;
	
	public GUI gui;
	public Window frame;
	public Graphics2D graphics;
	public KeyboardMouseListener keyboardMouseListener = new KeyboardMouseListener();
	public World world;
	public Entity player;
	public long totalFrames = 0;
	public int framesThisInterval = 0;
	public long intervalStartTime = 0;
	public int fps = 0;
	
	//Constants
	public static final int PIXELS_PER_BLOCK = 16;
	public static final double BLOCK_THRESHOLD = 1.0 / (2.0 * PIXELS_PER_BLOCK);
	public static final int TPS = 60; //The number of game ticks we want to calculate per second.
	public static final double defaultGravity = -0.8 / TPS; //Default entity downward acceleration due to "gravity", in blocks/tick^2.
	public static final double defaultAirResistance = 0.9; //Default entity horizontal damping multiplier due to "air resistance", multiplied by horizontal velocity every tick.
	public static final double defaultJumpAccel = 25.0 / TPS; //Default entity upward acceleration when jumping in blocks/tick^2.
	public static final double defaultHorAccel = 1.0 / TPS; //Default entity horizontal acceleration in blocks/tick^2.
	
	//Models
	Map<String, BlockModel> blockModels;
	Map<String, EntityModel> entityModels;
	
	//Threads
	public static Update update;
	public static Draw draw;
	public static Music music;
	
	/**
	 * Returns the instantiation of Core statically so we don't have to pass it everywhere.
	 */
	public static Core get(){
		final Core finalCore = core;
		return finalCore;
	}
	
	/**
	 * Main method
	 */
	public static void main(String[] args){
		core = new Core();
		core.run();
	}
	
	public void run(){
		try{
			init();
			gameLoop();
		}finally{
			//screenManager.restoreScreen();
		}
	}
	
	public void init(){
		running = true;
		
        //Set up the GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
            	gui = new GUI();
                gui.createAndShowGUI();
//            }
//        });
		
        //Set up the mouse and keyboard listeners.
		frame = gui.getFrame();
		frame.addKeyListener(keyboardMouseListener);
		frame.addMouseListener(keyboardMouseListener);
		frame.addMouseMotionListener(keyboardMouseListener);
		frame.addMouseWheelListener(keyboardMouseListener);
		
		//Load the model data.
		blockModels = YAMLParser.loadBlockModels("resources/blocks.yml");
		entityModels = YAMLParser.loadEntityModels("resources/entities.yml");
		
		world = new World("world1", 1000, 1000);
		try{
			Core.logf("Attempting to load world '%s'...", world.getName());
			world.load();
			Core.logf("Finished loading world '%s'", world.getName());
		}catch(FileNotFoundException e){
			Core.logf("World '%s' does not exist; generating new world", world.getName());
			world.generate();
		}catch(Exception e){
			e.printStackTrace();
			Core.logf("Error loading world '%s'; generating new world instead", world.getName());
			world.generate();
		}
		player = new Entity("player", world, new Vector(world.getWidth() / 2, world.getHeight() / 2 + 10));
		
		//Start the tick thread that processes all game logic.
		update = new Update();
		
		//Start the frame-drawing thread that shows the graphics.
		draw = new Draw(gui);
		
		//Start the music thread that plays the background music.
		music = new Music("resources/audio/music/thesalon.wav");
		//music.start();
	}
	
	public void exit(){
		try{
			Core.logf("Saving world '%s'...", world.getName());
			world.write();
			Core.logf("Finished saving world '%s'", world.getName());
		}catch(Exception e){
			e.printStackTrace();
			Core.logf("Error writing world '%s'", world.getName());
		}
		_exit();
	}

	@SuppressWarnings("deprecation")
	public static void _exit(){
		running = false;
		music.stop();
	}
	
	long previous = System.currentTimeMillis();
	long lag = 0;
	
	public void gameLoop(){
//		graphics = (Graphics2D) screenManager.getGraphics();
		while(running){
			update.update();
			draw.draw(fps, totalFrames);
			
			totalFrames++;
			framesThisInterval++;
			
			long timeDiff = (System.currentTimeMillis() - intervalStartTime);
			if(timeDiff >= 250){
				fps = (int) (framesThisInterval / (timeDiff / 1000.0));
				framesThisInterval = 0;
				intervalStartTime = System.currentTimeMillis();
			}

			try {
				Thread.sleep(System.currentTimeMillis() % (1000 / TPS) * -1 + (1000 / TPS));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		gui.getFrame().dispose();
	}
	
	/**
	 * Returns the coordinates of the upper left pixel of the block at the given coordinates.
	 */
	public Vector blocksToPixels(Vector coords){
		double playerX = player.getLocationX();
		double playerY = player.getLocationY();
		int width = gui.getWidth();
		int height = gui.getHeight();
		int ppb = PIXELS_PER_BLOCK;
		Vector returnVector = new Vector((int) ((coords.x - playerX) * ppb + (width / 2)), (int) ((-coords.y + playerY) * ppb + (height / 2)));
		return returnVector;
	}
	
	/**
	 * Returns the coordinates of the block occupying the pixel at the given coordinates.
	 */
	public Vector pixelsToBlocks(Vector coords){
		return new Vector((int) Math.floor((coords.x - (gui.getWidth() / 2)) / PIXELS_PER_BLOCK + player.getLocationX()), (int) (-Math.floor(coords.y - (gui.getHeight() / 2)) / PIXELS_PER_BLOCK + player.getLocationY() + 1));
	}
	
	/**
	 * Logs a message to the console and/or log file.
	 */
	public static void log(Object... string){
		String message = "";
		for(Object s : string){
			message += s.toString();
		}
		System.out.println(String.format("[%s] %s", new Date(System.currentTimeMillis()), message));
	}
	
	/**
	 * Logs a formatted message to the console and/or log file.
	 */
	public static void logf(String format, Object... args){
		String message = String.format(format, args);
		System.out.printf("[%s] %s\n", new Date(System.currentTimeMillis()), message);
	}
}