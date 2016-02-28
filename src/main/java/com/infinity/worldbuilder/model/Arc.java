package com.infinity.worldbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the <code>Arc</code>s between <code>Polygon</code>s. Contains the
 * two <code>Polygon</code>s it separates for the Delaunay and Voronoi shapes.
 * 
 * @author jeffreyrichley
 */
public class Arc implements Serializable {

	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = -5525087601313813026L;

	/**
	 * The two end points
	 */
	public final List<Node> endPoints = new ArrayList<Node>();
	
	/**
	 * The <code>Polygon</code>s that this <code>Arc</code> borders
	 */
	public final List<Polygon> borderingPolygons = new ArrayList<Polygon>();

	/**
	 * Creates a new <code>Arc</code> with the given end points
	 * @param nodeOne The first end point
	 * @param nodeTwo The second end point
	 */
	public Arc(Node nodeOne, Node nodeTwo) {
		endPoints.add(nodeOne);
		endPoints.add(nodeTwo);
	}

}
