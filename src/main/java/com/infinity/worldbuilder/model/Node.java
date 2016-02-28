package com.infinity.worldbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the corners of shapes.  Includes the list of <code>Polygon</code>s that
 * contain this <code>Node</code> as corners, <code>Arc</code>s that contain it as an endpoint,
 * and the <code>Node</code>s that can be accessed through the <code>Arc</code>s.
 * 
 * @author jeffreyrichley
 */
public class Node implements Serializable {

	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 4321547805577422186L;

	/**
	 * All <code>Polygon</code>s that have this <code>Node</code> as a corner
	 */
	public List<Polygon> touchingPolygons = new ArrayList<Polygon>();

	/**
	 * All <code>Edge</code>s that have this <code>Node</code> as an endpoint
	 */
	public List<Arc> protrudingArcs = new ArrayList<Arc>();

	/**
	 * All <code>Node</code>s that are connected by the protruding <code>Arc</code>s
	 */
	public List<Node> adjacentNodes = new ArrayList<Node>();

	/**
	 * The location of this <code>Node</code>
	 */
	public Point point;
	
	/**
	 * Creates a new <code>Node</code> 

	 * @param point The location of this <code>Node</code>
	 */
	public Node(Point point) {
		this.point = point;
	}

	public String toString() {
		return this.point.toString();
	}
}
