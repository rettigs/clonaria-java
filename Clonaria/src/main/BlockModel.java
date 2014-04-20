package main;

/**
 * Represents a type of block in a world.
 * The model only stores information generic to a block's type, not of its state.
 */
public class BlockModel extends Model{
	
	/*
	 * Attributes
	 */
	protected Integer depth;
	protected Boolean solid;
	protected Boolean normal;
	protected Integer icanattachto; // Bitmask, could be a byte
	protected Integer theycanattachto; // Bitmask, could be a byte
	protected Boolean breakondetach;
	protected Boolean gravity;
	protected Integer width;
	protected Integer height;
	protected Integer strength;
	protected Boolean explodable;
	protected Boolean flammable;
	protected Integer light;
	protected String drop;

	public int getDepth(){
		if(defaultmodel != null && depth == null) return ((BlockModel) defaultmodel).getDepth();
		else return depth;
	}

	public void setDepth(int depth){
		this.depth = depth;
	}

	public boolean getSolid(){
		if(defaultmodel != null && solid == null) return ((BlockModel) defaultmodel).getSolid();
		else return solid;
	}

	public void setSolid(boolean solid){
		this.solid = solid;
	}

	public boolean getNormal(){
		if(defaultmodel != null && normal == null) return ((BlockModel) defaultmodel).getNormal();
		else return normal;
	}

	public void setNormal(boolean normal){
		this.normal = normal;
	}

	public int getIcanattachto(){
		if(defaultmodel != null && icanattachto == null) return ((BlockModel) defaultmodel).getIcanattachto();
		else return icanattachto;
	}

	public void setIcanattachto(int icanattachto){
		this.icanattachto = icanattachto;
	}

	public int getTheycanattachto(){
		if(defaultmodel != null && theycanattachto == null) return ((BlockModel) defaultmodel).getTheycanattachto();
		else return theycanattachto;
	}

	public void setTheycanattachto(int theycanattachto){
		this.theycanattachto = theycanattachto;
	}

	public boolean getBreakondetach(){
		if(defaultmodel != null && breakondetach == null) return ((BlockModel) defaultmodel).getBreakondetach();
		else return breakondetach;
	}

	public void setBreakondetach(boolean breakondetach){
		this.breakondetach = breakondetach;
	}

	public boolean getGravity(){
		if(defaultmodel != null && gravity == null) return ((BlockModel) defaultmodel).getGravity();
		else return gravity;
	}

	public void setGravity(boolean gravity){
		this.gravity = gravity;
	}

	public int getWidth(){
		if(defaultmodel != null && width == null) return ((BlockModel) defaultmodel).getWidth();
		else return width;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight(){
		if(defaultmodel != null && height == null) return ((BlockModel) defaultmodel).getHeight();
		else return height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getStrength(){
		if(defaultmodel != null && strength == null) return ((BlockModel) defaultmodel).getStrength();
		else return strength;
	}

	public void setStrength(int strength){
		this.strength = strength;
	}

	public boolean getExplodable(){
		if(defaultmodel != null && explodable == null) return ((BlockModel) defaultmodel).getExplodable();
		else return explodable;
	}

	public void setExplodable(boolean explodable){
		this.explodable = explodable;
	}

	public boolean getFlammable(){
		if(defaultmodel != null && flammable == null) return ((BlockModel) defaultmodel).getFlammable();
		else return flammable;
	}

	public void setFlammable(boolean flammable){
		this.flammable = flammable;
	}

	public int getLight(){
		if(defaultmodel != null && light == null) return ((BlockModel) defaultmodel).getLight();
		else return light;
	}

	public void setLight(int light){
		this.light = light;
	}

	public String getDrop(){
		if(defaultmodel != null && drop == null) return ((BlockModel) defaultmodel).getDrop();
		else return drop;
	}

	public void setDrop(String drop){
		this.drop = drop;
	}
	
	public boolean iCanAttachFront(){
		if((getIcanattachto() & 1) != 0) return true;
		else return false;
	}
	
	public boolean iCanAttachBack(){
		if((getIcanattachto() & 2) != 0) return true;
		else return false;
	}
	
	public boolean iCanAttachTop(){
		if((getIcanattachto() & 4) != 0) return true;
		else return false;
	}
	
	public boolean iCanAttachRight(){
		if((getIcanattachto() & 8) != 0) return true;
		else return false;
	}
	
	public boolean iCanAttachBottom(){
		if((getIcanattachto() & 16) != 0) return true;
		else return false;
	}
	
	public boolean iCanAttachLeft(){
		if((getIcanattachto() & 32) != 0) return true;
		else return false;
	}
	
	public boolean theyCanAttachFront(){
		if((getTheycanattachto() & 1) != 0) return true;
		else return false;
	}
	
	public boolean theyCanAttachBack(){
		if((getTheycanattachto() & 2) != 0) return true;
		else return false;
	}
	
	public boolean theyCanAttachTop(){
		if((getTheycanattachto() & 4) != 0) return true;
		else return false;
	}
	
	public boolean theyCanAttachRight(){
		if((getTheycanattachto() & 8) != 0) return true;
		else return false;
	}
	
	public boolean theyCanAttachBottom(){
		if((getTheycanattachto() & 16) != 0) return true;
		else return false;
	}
	
	public boolean theyCanAttachLeft(){
		if((getTheycanattachto() & 32) != 0) return true;
		else return false;
	}
}