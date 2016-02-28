package com.infinity.worldbuilder.util;

import java.util.Arrays;

import com.infinity.worldbuilder.model.Arc;
import com.infinity.worldbuilder.model.Node;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.model.Point;
import com.infinity.worldbuilder.model.Polygon;

public class PanUtil {
	
	private PanGraph graph;

	public PanUtil() {
		graph = new PanGraph();
	}

	public PanGraph getGraph() {
		return graph;
	}

	public Node getNode(Point pt) {
		Node node = null;
		// lets see if we already have a node with that point
		for (Node n : graph.nodes) {
			if (n.point.x == pt.x && n.point.y == pt.y && n.point.z == pt.z) {
				node = n;
				break;
			}
		}
		
		// if we didn't find it in the list of nodes, create a new one
		if (node == null) {
			node = new Node(pt);
			// don't forget to add to the list of nodes
			graph.nodes.add(node);
		}
		return node;
	}

	public Arc getArc(Node n1, Node n2) {
		Arc arc = null;
		// lets see if we can find one created from before
		for (Arc a : graph.arcs) {
			if (a.endPoints.contains(n1) && a.endPoints.contains(n2)) {
				arc = a;
				break;
			}
		}
		
		// if we couldn't find an old one, create a new one
		if (arc == null) {
			arc = new Arc(n1, n2);
			graph.arcs.add(arc);
			n1.protrudingArcs.add(arc);
			n2.protrudingArcs.add(arc);
			n1.adjacentNodes.add(n2);
			n2.adjacentNodes.add(n1);
		}
		
		return arc;
	}

	public Polygon getPolygon(Node... nodes) {
		// must have 3 or more sides to be a polygon
		if (nodes.length <= 2) {
			throw new IllegalArgumentException("Polygons must have at least 3 nodes, this has only " + nodes.length);
		}
		
		Polygon poly = null;
		// lets see if we can find a previously created polygon
		for (Polygon p : graph.polygons) {
			if (p.corners.containsAll(Arrays.asList(nodes))) {
				poly = p;
				break;
			}
		}
		
		// if we couldn't find an old one, create a new one
		if (poly == null) {
			poly = new Polygon();
			graph.polygons.add(poly);
			// connect the arcs
			// do all but the last one
			for (int i = 0; i < nodes.length - 1; i++) {
				Arc arc = getArc(nodes[i], nodes[i+1]);
				poly.borders.add(arc);
				arc.borderingPolygons.add(poly);
			}
			// now connect the last and the first
			Arc arc = getArc(nodes[nodes.length-1], nodes[0]);
			poly.borders.add(arc);
			arc.borderingPolygons.add(poly);
			
			// connect neighboring polygons
			for (Arc a : poly.borders) {
				for (Polygon p : a.borderingPolygons) {
					if (p != poly) {
						poly.neighbors.add(p);
						p.neighbors.add(poly);
					}
				}
			}
			
			// connect the nodes
			for (Node node : nodes) {
				poly.corners.add(node);
				node.touchingPolygons.add(poly);
			}
		}
		return poly;
	}

	public void removePolygon(Polygon poly) {
		// unwire polygons
		graph.polygons.remove(poly);
		for (Polygon p : poly.neighbors) {
			p.neighbors.remove(poly);
		}
		poly.neighbors.clear();
		
		// unwire arcs
		for (Arc a : poly.borders) {
			a.borderingPolygons.remove(poly);
			if (a.borderingPolygons.isEmpty()) {
				graph.arcs.remove(a);
				for (Node n : a.endPoints) {
					n.protrudingArcs.remove(a);
				}
				a.endPoints.clear();
			}
		}
		poly.borders.clear();
		
		// unwire nodes
		for (Node n : poly.corners) {
			n.touchingPolygons.remove(poly);
			if (n.touchingPolygons.isEmpty()) {
				graph.nodes.remove(n);
				for (Node other : n.adjacentNodes) {
					other.adjacentNodes.remove(n);
				}
				n.adjacentNodes.clear();
			}
		}
		poly.corners.clear();
	}

}
