package com.infinity.worldbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.infinity.worldbuilder.util.PanUtil;

/**
 * Holds references to all of the components of the graph
 * 
 * @author jeffreyrichley
 */
public class PanGraph implements Serializable {

	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 4543618880619187203L;

	/**
	 * All <code>Polygon</code>s in the graph
	 */
	public List<Polygon> polygons = new ArrayList<Polygon>();
	
	/**
	 * All <code>Arc</code>s in the graph
	 */
	public List<Arc> arcs = new ArrayList<Arc>();
	
	/**
	 * All <code>Node</code>s in the graph
	 */
	public List<Node> nodes = new ArrayList<Node>();

	public final PanUtil util;

	public PanGraph(PanUtil util) {
		this.util = util;
	}
}
