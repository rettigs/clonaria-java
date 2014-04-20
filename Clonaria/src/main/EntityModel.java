package main;

import java.awt.Polygon;

import enums.EntityAI;
import enums.EntityPhysics;

/**
 * Represents a non-block object in a world.  Only holds context-free data; there is only one of each type ever present.
 */
public class EntityModel extends Model{
	
	/*
	 * Attributes
	 */
	protected Polygon hitbox; // A polygon representing the shape of the entity (in pixel coordinates)
	protected EntityPhysics physicsmodel; // The entity's physics model
	protected EntityAI aimodel; // The entity's AI model
	protected Integer light;

	public Polygon getHitbox(){
		if(defaultmodel != null && hitbox == null) return ((EntityModel) defaultmodel).getHitbox();
		else return hitbox;
	}

	public void setHitbox(Polygon hitbox){
		this.hitbox = hitbox;
	}

	public EntityPhysics getPhysicsmodel(){
		if(defaultmodel != null && physicsmodel == null) return ((EntityModel) defaultmodel).getPhysicsmodel();
		else return physicsmodel;
	}

	public void setPhysicsmodel(EntityPhysics physicsmodel){
		this.physicsmodel = physicsmodel;
	}

	public EntityAI getAimodel(){
		if(defaultmodel != null && aimodel == null) return ((EntityModel) defaultmodel).getAimodel();
		else return aimodel;
	}

	public void setAimodel(EntityAI aimodel){
		this.aimodel = aimodel;
	}

	public Integer getLight(){
		if(defaultmodel != null && light == null) return ((EntityModel) defaultmodel).getLight();
		else return light;
	}

	public void setLight(Integer light){
		this.light = light;
	}
}