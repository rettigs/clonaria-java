package main;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Loads game data from YAML files and instantiates corresponding flywheels.
 */
public class YAMLParser{
	
	/**
	 *  Creates a model for each one specified in the YAML document.
	 *  Returns a HashMap of all models.
	 */
	public static Map<String, Model> loadModels(String modelType, String path){
		Map<String, Model> models = new HashMap<String, Model>();
		Yaml yaml = null;
		if(modelType.equals("block")) yaml = new Yaml(new Constructor(BlockModel.class));
		else if(modelType.equals("entity"))	yaml = new Yaml(new Constructor(EntityModel.class));
		
		InputStream input = null;
		try{
			input = new FileInputStream(new File(path));
		}catch(FileNotFoundException e){
			Core.log("Could not load "+modelType+" models from "+path);
			Core.get().exit();
		}
		Core.log("Loading "+modelType+" models from "+path);
		
		Model defaultModel = null;
		int counter = -1;
		for(Object data : yaml.loadAll(input)){
			counter++;
			Model model = (Model) data;
			
			if(counter == 0){ // The default model comes first
				if(!model.getType().equals("default")){ // If the first one isn't the default model, we exit. TODO?: Read file twice to get the default first, no matter where it is (instead of just exiting)
					Core.log("First "+modelType+" model in "+path+" isn't the default model; exiting");
					Core.get().exit();
				}else{ // Otherwise, make it the default model
					defaultModel = model;
				}
			}else{ // If we're not the default model, set our default to the default model
				model.setDefaultmodel(defaultModel);
			}
			
			// Load the texture file for the model
			String texturePath = model.getTexturepath();
			Image texture = null;
			Core.log("Reading "+modelType+" texture from '"+texturePath+"'");
			try{
				texture = ImageIO.read(new File(texturePath));
			}catch(Exception e1){
				Core.log("Could not read "+modelType+" texture at '"+texturePath+"'; using "+modelType+" name '"+model.getType()+"'");
				if(modelType.equals("block")) texturePath = "resources/images/blocks/"+model.getType()+".png";
				else if(modelType.equals("entity"))	texturePath = "resources/images/entities/"+model.getType()+".png";
				Core.log("Reading "+modelType+" texture from '"+texturePath+"'");
				try{
					texture = ImageIO.read(new File(texturePath));
				}catch(Exception e2){
					Core.log("Could not read "+modelType+" texture at '"+texturePath+"'; using default");
					if(modelType.equals("block")) texturePath = "resources/images/blocks/default.png";
					else if(modelType.equals("entity"))	texturePath = "resources/images/entities/default.png";
					Core.log("Reading "+modelType+" texture from '"+texturePath+"'");
					try{
						texture = ImageIO.read(new File(texturePath));
					}catch(Exception e3){
						Core.log("Could not read "+modelType+" texture at '"+texturePath+"'; exiting");
						Core.get().exit();
					}
				}
			}
			model.setTexture(texture);
			
			// If it's an entity, invalidate the cache in the hitbox since we modified its internal data structures when creating it through SnakeYAML
			if(modelType.equals("entity")) ((EntityModel) model).getHitbox().invalidate();
			
			// Add it to the HashMap
			models.put(model.getType(), model);
		}

		Core.log("Loaded "+counter+" "+modelType+" models");

		return models;
	}

	/**
	 *  Creates a BlockModel for each one specified in the YAML document.
	 *  Returns a HashMap of all BlockModels.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, BlockModel> loadBlockModels(String path){
		return (Map<String, BlockModel>) (Object) loadModels("block", path);
	}
	
	/**
	 *  Creates an EntityModel for each one specified in the YAML document.
	 *  Returns a HashMap of all EntityModels.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, EntityModel> loadEntityModels(String path){
		return (Map<String, EntityModel>) (Object) loadModels("entity", path);
	}
}
