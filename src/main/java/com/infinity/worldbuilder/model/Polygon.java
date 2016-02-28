package com.infinity.worldbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multi-edged, closed <code>Polygon</code>.  Contains lists for its
 * neighboring <code>Polygon</code>, border <code>Arc</code>s, and <code>Node</code>s.
 * @author jeffreyrichley
 */
public class Polygon implements Serializable {

	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 1018418823921336736L;

	/**
	 * Adjacent <code>Polygon</code>s
	 */
	public List<Polygon> neighbors = new ArrayList<Polygon>();

	/**
	 * Bordering <code>Arc</code>s
	 */
	public List<Arc> borders = new ArrayList<Arc>();

	/**
	 * Corners of this <code>Polygon</code>
	 */
	public List<Node> corners = new ArrayList<Node>();

	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (Node node : corners) {
			buff.append(node.toString()).append("\n");
		}
		return buff.toString();
	}
}
