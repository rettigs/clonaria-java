package main;

import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import enums.EntityAI;
import enums.EntityPhysics;

import main.structs.Vector;

/**
 * Represents a non-block object in a world.  Uses an EntityModel as a flyweight to store stateless data.
 */
public class Entity{
	
	/*
	 * Attributes
	 */
	private String name; // The name of this entity (e.g. "xXsupan00b69Xx", but usually null)
	private EntityModel entityModel; // The model of this entity (stores stateless data)
	private World world; // The world the entity is in
	private Vector location; // The entity's location in the world (in block coordinates)
	public boolean againstBlockUp; // Whether the entity is against a block above it
	public boolean againstBlockDown; // Whether the entity is against a block below it
	public boolean againstBlockLeft; // Whether the entity is against a block left of it
	public boolean againstBlockRight; // Whether the entity is against a block right of it
	private Vector velocity; // The entity's velocity, in blocks per tick
	private Entity target; // The entity's target; the entity will attempt to move toward it
	private double gravity; // The entity's downward acceleration due to "gravity", in blocks/tick^2
	private double airResistance; // The entity's horizontal damping multiplier due to "air resistance", in dampAmount/tick
	private double jumpAccel; // The entity's upward acceleration when jumping in blocks/tick^2
	private double horAccel; // The entity's horizontal acceleration in blocks/tick^2
	private int maxJumps; // How many times the entity is allowed to jump (usually 1)
	private int curJumps; // How many times the entity has jumped.  Resets upon contact with ground
	private int maxHealth; // How much health the entity can have
	private int curHealth; // How much health the entity currently has
	private int defense; // How much defense the entity has
	private int light; // How bright this entity is
	private Map<String, Item> inventory; // Holds the inventory of the entity; mostly used for players and to hold mob drops
	private String holding; // The key to the inventory item the entity is currently holding
	
	double tolerance = 1.0 / (Core.PIXELS_PER_BLOCK * 2);

	public Entity(String entityModelType, World world, Vector location){
		this.entityModel = Core.get().entityModels.get(entityModelType);
		this.world = world;
		this.location = location;
		this.velocity = new Vector(0, 0);
		
		this.gravity = Core.defaultGravity;
		this.airResistance = Core.defaultAirResistance;
		this.jumpAccel = Core.defaultJumpAccel;
		this.horAccel = Core.defaultHorAccel;
	}
	
	public EntityModel getEntityModel(){
		return this.entityModel;
	}
	
	public void setEntityModel(EntityModel entityModel){
		this.entityModel = entityModel;
	}
	
	public String getType(){
		return entityModel.getType();
	}

	public EntityPhysics getPhysicsModel(){
		return entityModel.getPhysicsmodel();
	}

	public EntityAI getAIModel(){
		return entityModel.getAimodel();
	}

	public Image getTexture(){
		return entityModel.getTexture();
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public World getWorld(){
		return world;
	}

	public Vector getLocation(){
		return location;
	}
	
	public double getLocationX(){
		return location.x;
	}

	public void setLocationX(double x){
		this.location.x = x;
	}
	
	public double getLocationY(){
		return location.y;
	}

	public void setLocationY(double y){
		this.location.y = y;
	}

	public void addLocationX(double x){
		setLocationX(getLocationX() + x);
	}

	public void addLocationY(double y){
		setLocationY(getLocationY() + y);
	}
	
	/**
	 * Returns the entity's hitbox as of the given number of ticks into the future, based on the given velocity (in blocks per second).
	 * The hitbox is a polygon representing the shape and location of the entity (in pixel coordinates) as it would be on-screen.
	 */
	public Polygon getHitbox(int ticks, Vector vel){
		Vector testLocation = location.plus(vel.x * ticks, vel.y * ticks);
		Vector offset = Core.get().blocksToPixels(testLocation);
		Polygon modelHitbox = entityModel.getHitbox();
		Polygon hitbox = new Polygon(modelHitbox.xpoints, modelHitbox.ypoints, modelHitbox.npoints);
		hitbox.translate((int) offset.x, (int) offset.y);
		return hitbox;
	}
	
	/**
	 * Returns the entity's size (bounding box dimensions in blocks).
	 */
	public Vector getSize(){
		Rectangle bounds = entityModel.getHitbox().getBounds();
		return new Vector((bounds.width + 1.0) / Core.PIXELS_PER_BLOCK, (bounds.height + 1.0) / Core.PIXELS_PER_BLOCK);
	}
	
	/**
	 * Gets all blocks near the entity as of the given number of ticks into the future, based on the given velocity (in blocks per second).
	 */
	public Set<Vector> getNearbyBlocks(int ticks, Vector vel){
		Set<Vector> blocks = new HashSet<Vector>();
		Vector size = getSize();
		for(int y = (int) (getLocationY() - 1 + vel.y * ticks); y < getLocationY() - 1 + vel.y * ticks + size.y; y++){
			for(int x = (int) (getLocationX() + vel.x * ticks); x < getLocationX() + vel.x * ticks + size.x; x++){
				blocks.add(new Vector(x, y));
			}
		}		
		return blocks;
	}
	
//	public Set<Vector> getNearbyBlocks(int ticks, Vector vel){
//		Set<Vector> blocks = new HashSet<Vector>();
//		Rectangle bounds = entityModel.getHitbox().getBounds();
//		for(int y = -1; y < 1 + Math.ceil((bounds.width + 1) * 1.0 / Core.PIXELS_PER_BLOCK); y++){
//			for(int x = 0; x < -1 + Math.ceil((bounds.height + 1) * 1.0 / Core.PIXELS_PER_BLOCK); x++){
//				blocks.add(new Vector(Math.floor(getLocationX() + x), Math.floor(getLocationY() + y)));
//			}
//		}		
//		return blocks;
//	}
	
	/**
	 * Gets all blocks above the entity as of the given number of ticks into the future, based on the given velocity (in blocks per second).
	 */
	public Set<Vector> getBlocksUp(int ticks, Vector vel){
		Set<Vector> blocks = new HashSet<Vector>();
		Vector size = getSize();
		double min = getLocationX() + (vel.x * ticks) + Core.BLOCK_THRESHOLD;
		double max = getLocationX() + (vel.x * ticks) - Core.BLOCK_THRESHOLD + size.x;
		for(int x = (int) min; x < max; x++){
			blocks.add(new Vector((int) x, Math.ceil(getLocationY() + size.y - 1)));
		}	
		return blocks;
	}
	
	/**
	 * Gets all blocks below the entity as of the given number of ticks into the future, based on the given velocity (in blocks per second).
	 */
	public Set<Vector> getBlocksDown(int ticks, Vector vel){
		Set<Vector> blocks = new HashSet<Vector>();
		Vector size = getSize();
		double min = getLocationX() + (vel.x * ticks) + Core.BLOCK_THRESHOLD;
		double max = getLocationX() + (vel.x * ticks) - Core.BLOCK_THRESHOLD + size.x;
		for(int x = (int) min; x < max; x++){
			blocks.add(new Vector((int) x, Math.floor(getLocationY() - size.y)));
		}	
		return blocks;
	}
	
	/**
	 * Gets all blocks left of the entity as of the given number of ticks into the future, based on the given velocity (in blocks per second).
	 */
	public Set<Vector> getBlocksLeft(int ticks, Vector vel){
		Set<Vector> blocks = new HashSet<Vector>();
		Vector size = getSize();
		double min = getLocationY() + (vel.y * ticks) + Core.BLOCK_THRESHOLD;
		double max = getLocationY() + (vel.y * ticks) - Core.BLOCK_THRESHOLD + size.y;
		for(int y = (int) min; y < max; y++){
			blocks.add(new Vector(Math.floor(getLocationX() - getSize().x), y - 1));
		}	
		return blocks;
	}
	
	/**
	 * Gets all blocks right of the entity as of the given number of ticks into the future, based on the given velocity (in blocks per second).
	 */
	public Set<Vector> getBlocksRight(int ticks, Vector vel){
		Set<Vector> blocks = new HashSet<Vector>();
		Vector size = getSize();
		double min = getLocationY() + (vel.y * ticks) + Core.BLOCK_THRESHOLD;
		double max = getLocationY() + (vel.y * ticks) - Core.BLOCK_THRESHOLD + size.y;
		for(int y = (int) min; y < max; y++){
			blocks.add(new Vector(Math.ceil(getLocationX() + getSize().x), y - 1));
		}	
		return blocks;
	}
	
	/**
	 * Returns a clone of this entity's velocity vector.
	 */
	public Vector getVelocity(){
		return velocity.clone();
	}
	
	public double getVelocityX(){
		return velocity.x;
	}

	public void setVelocityX(double x){
		this.velocity.x = x;
	}
	
	public double getVelocityY(){
		return velocity.y;
	}

	public void setVelocityY(double y){
		this.velocity.y = y;
	}

	public void addVelocityX(double x){
		setVelocityX(getVelocityX() + x);
	}

	public void addVelocityY(double y){
		setVelocityY(getVelocityY() + y);
	}

	public Entity getTarget(){
		return target;
	}

	public void setTarget(Entity target){
		this.target = target;
	}

	public int getMaxJumps(){
		return maxJumps;
	}

	public void setMaxJumps(int maxJumps){
		this.maxJumps = maxJumps;
	}

	public int getCurJumps(){
		return curJumps;
	}

	public void setCurJumps(int curJumps){
		this.curJumps = curJumps;
	}

	public boolean getInAir(){
		return !againstBlockDown;
	}
	
	public void left(){
		addVelocityX(-horAccel);
	}
	
	public void right(){
		addVelocityX(horAccel);
	}
	
	public void jump(){
		if(againstBlockDown){
			addVelocityY(jumpAccel);
			againstBlockDown = false;
		}
	}
	
	public void stopX(){
		setVelocityX(0);
	}
	
	public void stopY(){
		setVelocityY(0);
	}
	
	/**
	 * Apply n tick's worth of acceleration due to gravity to this entity.
	 */	
	public void applyGravity(int ticks){
		switch(entityModel.getPhysicsmodel()){
			case BOUNCY:
			case LIQUID:
			case PHASING:
			case SOLID:
				//Accelerate downward if in midair.
				if(getInAir()) addVelocityY(gravity * ticks);
			default:
				break;
		}
	}
	
	/**
	 * Apply n tick's worth of acceleration due to air resistance to this entity.
	 */	
	public void applyAirResistance(int ticks){
		switch(entityModel.getPhysicsmodel()){
			case SOLID:
				//Reduce x velocity.
				setVelocityX(getVelocityX() * Math.pow(airResistance, ticks));
			default:
				break;
		}
	}
	
	/**
	 * Apply n tick's worth of movement based on its velocity, location, and type.
	 */
	public void move(int ticks){
		switch(entityModel.getPhysicsmodel()){
			case SOLID:
				boolean isFreeX = !willCollide(ticks, new Vector(this.getVelocityX(), 0));
				boolean isFreeY = !willCollide(ticks, new Vector(0, this.getVelocityY()));
				
				// In case the entity hits a corner instead of just a wall or a floor/ceiling.
//				if(willCollide(ticks, new Vector(this.getVelocityX(), this.getVelocityY()))){
//					isFreeX = false;
//					isFreeY = false;
//				}
				
				// If they are free, move.  Otherwise, stop.
				if(isFreeX){
					if(getVelocityX() >= 0){
						if(!againstBlockRight) addLocationX(getVelocityX() * ticks);
					}else{
						if(!againstBlockLeft) addLocationX(getVelocityX() * ticks);
					}
				}else{
					if(getVelocityX() > 0){
						againstBlockRight = true;
					}else{
						againstBlockLeft = true;
					}
					stopX();
					//setLocationX(Math.round(getLocationX()));
				}
				
				if(isFreeY){
					if(getVelocityY() >= 0){
						if(!againstBlockUp) addLocationY(getVelocityY() * ticks);
					}else{
						if(!againstBlockDown) addLocationY(getVelocityY() * ticks);
					}
				}else{
					if(getVelocityY() > 0) againstBlockUp = true;
					else againstBlockDown = true;
					stopY();
					setLocationY(Math.round(getLocationY()));
				}
			default:
				break;
		}
		
	}
	
	public void updateAgainstBlocks(int ticks, Vector vel){
		updateAgainstBlockUp(ticks, vel);
		updateAgainstBlockDown(ticks, vel);
		updateAgainstBlockLeft(ticks, vel);
		updateAgainstBlockRight(ticks, vel);
	}
	
	public void updateAgainstBlockUp(int ticks, Vector vel){
		boolean against = false;
		for(Vector block : getBlocksUp(ticks, vel)){
			if(block.y - (block.x - 1) <= Core.BLOCK_THRESHOLD && getWorld().isSolidAt((int) block.x, (int) block.y, 1)){
				against = true;
				this.setLocationY(Math.round(this.getLocationY()));
				break;
			}
		}
		againstBlockUp = against;
	}
	
	public void updateAgainstBlockDown(int ticks, Vector vel){
		boolean against = false;
		for(Vector block : getBlocksDown(ticks, vel)){
			if(getLocation().y - getSize().y - block.y <= Core.BLOCK_THRESHOLD && getWorld().isSolidAt((int) block.x, (int) block.y, 1)){
				against = true;
				this.setLocationY(Math.round(this.getLocationY()));
				break;
			}
		}
		againstBlockDown = against;
	}
	
	public void updateAgainstBlockLeft(int ticks, Vector vel){
		boolean against = false;
		for(Vector block : getBlocksLeft(ticks, vel)){
			if(getLocation().x - (block.x + 1) <= Core.BLOCK_THRESHOLD && getWorld().isSolidAt((int) block.x, (int) block.y, 1)){
				against = true;
				this.setLocationX(Math.round(this.getLocationX()));
				break;
			}
		}
		againstBlockLeft = against;
	}
	
	public void updateAgainstBlockRight(int ticks, Vector vel){
		boolean against = false;
		for(Vector block : getBlocksRight(ticks, vel)){
			if(block.x - (getLocation().x + getSize().x) <= Core.BLOCK_THRESHOLD && getWorld().isSolidAt((int) block.x, (int) block.y, 1)){
				against = true;
				this.setLocationX(Math.round(this.getLocationX()));
				break;
			}
		}
		againstBlockRight = against;
	}
	
//	public void updateAgainstBlocks(){
//		Vector size = getSize();
//		boolean actuallyAgainst;
//		
//		if(againstBlockUp){
//			actuallyAgainst = false;
//			for(double i = location.x; i <= location.x + size.x; i++){
//				if(getWorld().isSolidAt((int) i, (int) (location.y + 1), 1)){
//					actuallyAgainst = true;
//					break;
//				}
//			}
//			againstBlockUp = actuallyAgainst;
//		}
//		
//		if(againstBlockLeft){
//			actuallyAgainst = false;
//			for(double i = location.y; i <= location.y + size.y; i++){
//				if(getWorld().isSolidAt((int) (location.x - 1), (int) i, 1)){
//					actuallyAgainst = true;
//					break;
//				}
//			}
//			againstBlockLeft = actuallyAgainst;
//		}
//		
//		if(againstBlockRight){
//			actuallyAgainst = false;
//			for(double i = location.y; i <= location.y + size.y; i++){
//				if(getWorld().isSolidAt((int) (location.x + size.x), (int) i, 1)){
//					actuallyAgainst = true;
//					break;
//				}
//			}
//			againstBlockRight = actuallyAgainst;
//		}
//	}
	
	/**
	 * Returns whether the entity will collide with the world within n ticks, based on the given velocity (in blocks per second).
	 */
	public boolean willCollide(int ticks, Vector vel){
		boolean collides = false;
		
		// Test all nearby blocks for whether they intersect the entity's hitbox
		Polygon hitbox = getHitbox(ticks, vel);
		Set<Vector> nearbyBlocks = getNearbyBlocks(ticks, vel);
		for(Vector block : nearbyBlocks){
			if(getWorld().isSolidAt((int) block.x, (int) block.y, 1)){
				Vector pixelCoords = Core.get().blocksToPixels(block);
				Rectangle blockRect = new Rectangle((int) pixelCoords.x, (int) pixelCoords.y, Core.PIXELS_PER_BLOCK, Core.PIXELS_PER_BLOCK);
				if(hitbox.intersects(blockRect)){
					collides = true;
					break;
				}
			}
		}
		
		return collides;
	}
}