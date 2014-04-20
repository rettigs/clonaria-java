package main;

import java.awt.Image;

/**
 * Represents a type of thing in a world.
 * The model only stores information generic to a type of thing, not of its state.
 */
public class Model{
	
	/*
	 * Attributes
	 */
	protected String defaultmodeltype;
	protected Model defaultmodel;
	protected String type;
	protected String texturepath;
	protected Image texture;

	public String getDefaultmodeltype(){
		if(defaultmodel != null && defaultmodeltype == null) return defaultmodel.getDefaultmodeltype();
		else return defaultmodeltype;
	}

	public void setDefaultmodeltype(String defaultmodeltype){
		this.defaultmodeltype = defaultmodeltype;
	}

	public Model getDefaultmodel(){
		if(defaultmodel != null && defaultmodel == null) return defaultmodel.getDefaultmodel();
		else return defaultmodel;
	}

	public void setDefaultmodel(Model defaultmodel){
		this.defaultmodel = defaultmodel;
	}

	public String getType(){
		if(defaultmodel != null && type == null) return defaultmodel.getType();
		else return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getTexturepath(){
		if(defaultmodel != null && texturepath == null) return defaultmodel.getTexturepath();
		else return texturepath;
	}

	public void setTexturepath(String texturePath){
		this.texturepath = texturePath;
	}

	public Image getTexture(){
		if(defaultmodel != null && texture == null) return defaultmodel.getTexture();
		else return texture;
	}

	public void setTexture(Image texture){
		this.texture = texture;
	}
}