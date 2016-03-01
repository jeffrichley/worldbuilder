package com.infinity.worldbuilder;

import com.infinity.worldbuilder.actions.ActivityChain;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.util.ModelUtil;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public class WorldBuilder extends SimpleApplication {
	
	private Boolean isRunning = true;
	private Geometry geom = null;
	
	// Tweak parameters
	int subdivisions = 4;

	@Override
	public void simpleInitApp() {
		ActivityChain chain = new ActivityChain();
		
		chain.addActivity(new IcosahedronGeneratorActivity());
		chain.addActivity(new SubdivisionActivity(subdivisions));
		chain.addActivity(new TessellationActivity(.05));
		
		PanGraph graph = chain.execute(null).getGraph();
		
//		PanGraph icosahedron = ModelUtil.generateIcosahedron();
//		PanGraph icosahedron2 = ModelUtil.generateSubdividedIcosahedron(subdivisions, icosahedron);
		
//		PanMarshaller m = new PanMarshaller();
//		PanUnmarshaller u = new PanUnmarshaller();
//		m.marshal(icosahedron2, "tmp.graph");
//		PanGraph tmp = u.unmarshal("tmp.graph");
		
//		Mesh mesh = ModelUtil.graphToMesth(icosahedron2);
		Mesh mesh = ModelUtil.graphToMesth(graph);
		
		Material mat = new Material(assetManager,
		          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
//        mat.getAdditionalRenderState().setWireframe(true);
        
        geom = new Geometry("Box", mesh);  // create cube geometry from the shape
        geom.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(geom);              // make the cube appear in the scene
        
        initKeys();
	}
	
	/** Custom Keybinding: Map named actions to inputs. */
	  private void initKeys() {
	    // You can map one or several inputs to one named action
	    inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
	    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J));
	    inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_K));
	    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
	                                      new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	    // Add the names to the action listener.
	    inputManager.addListener(actionListener,"Pause");
	    inputManager.addListener(analogListener,"Left", "Right", "Rotate");
	 
	  }
	  
	  private ActionListener actionListener = new ActionListener() {
		    public void onAction(String name, boolean keyPressed, float tpf) {
		      if (name.equals("Pause") && !keyPressed) {
		        isRunning = !isRunning;
		      }
		    }
		  };
	  
	  private AnalogListener analogListener = new AnalogListener() {
		    public void onAnalog(String name, float value, float tpf) {
		      if (isRunning) {
		        if (name.equals("Rotate")) {
		        	geom.rotate(0, value*speed, 0);
		        }
		        if (name.equals("Right")) {
		          Vector3f v = geom.getLocalTranslation();
		          geom.setLocalTranslation(v.x + value*speed, v.y, v.z);
		        }
		        if (name.equals("Left")) {
		          Vector3f v = geom.getLocalTranslation();
		          geom.setLocalTranslation(v.x - value*speed, v.y, v.z);
		        }
		      } else {
		        System.out.println("Press P to unpause.");
		      }
		    }
		  };

	public static void main(String[] args){
		WorldBuilder app = new WorldBuilder();
	    app.start(); // start the game
	}
	
}
