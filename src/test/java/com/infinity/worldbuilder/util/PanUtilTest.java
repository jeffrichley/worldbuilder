package com.infinity.worldbuilder.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.infinity.worldbuilder.model.Arc;
import com.infinity.worldbuilder.model.Node;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.model.Point;
import com.infinity.worldbuilder.model.Polygon;

public class PanUtilTest {

	private PanGraph graph;
	private PanUtil util;

	@Before
	public void setUp() throws Exception {
		util = new PanUtil();
		graph = util.getGraph();
	}

	@Test
	public void ensureCanCreateGraph() {
		assertThat(graph, is(not(nullValue())));
	}
	
	@Test
	public void ensureCanGetNode() {
		Point point = new Point(0, 0);
		
		Node node = util.getNode(point);
		
		assertThat(node, is(not(nullValue())));
		assertTrue(graph.nodes.contains(node));
	}
	
	@Test
	public void ensureCanCacheNodes() {
		Point point = new Point(0, 0);
		
		Node nodeOne = util.getNode(point);
		Node nodeTwo = util.getNode(point);
		
		assertSame(nodeOne, nodeTwo);
		assertThat(graph.nodes.size(), is(equalTo(1)));
	}
	
	@Test
	public void ensureCanCreateArc() {
		Point pt1 = new Point(0, 0);
		Point pt2 = new Point(1, 1);
		Node n1 = util.getNode(pt1);
		Node n2 = util.getNode(pt2);
		
		Arc arc = util.getArc(n1, n2);
		
		assertThat(arc, is(not(nullValue())));
		assertTrue(arc.endPoints.contains(n1));
		assertTrue(arc.endPoints.contains(n2));
		assertTrue(graph.arcs.contains(arc));
		assertTrue(n1.protrudingArcs.contains(arc));
		assertTrue(n2.protrudingArcs.contains(arc));
		assertTrue(n1.adjacentNodes.contains(n2));
		assertTrue(n2.adjacentNodes.contains(n1));
	}
	
	@Test
	public void ensureCanCacheArcs() {
		Point pt1 = new Point(0, 0);
		Point pt2 = new Point(1, 1);
		Node n1 = util.getNode(pt1);
		Node n2 = util.getNode(pt2);

		Arc original = util.getArc(n1, n2);
		Arc one = util.getArc(n1, n2);
		Arc two = util.getArc(n2, n1);
		
		assertSame(original, one);
		assertSame(original, two);
	}
	
	@Test
	public void ensureCanCreatePolygon() {
		Point pt1 = new Point(0, 0);
		Point pt2 = new Point(1, 1);
		Point pt3 = new Point(0, 1);
		Node n1 = util.getNode(pt1);
		Node n2 = util.getNode(pt2);
		Node n3 = util.getNode(pt3);
		
		Polygon poly = util.getPolygon(n1, n2, n3);
		
		assertThat(poly, is(not(nullValue())));
		assertTrue(graph.polygons.contains(poly));
		assertThat(poly.borders.size(), is(3));
		assertThat(graph.arcs.size(), is(3));
		assertThat(poly.corners.size(), is(3));
		assertThat(graph.nodes.size(), is(3));
		assertTrue(poly.borders.get(0).borderingPolygons.contains(poly));
		assertTrue(poly.borders.get(1).borderingPolygons.contains(poly));
		assertTrue(poly.borders.get(2).borderingPolygons.contains(poly));
		assertTrue(poly.corners.get(0).touchingPolygons.contains(poly));
		assertTrue(poly.corners.get(1).touchingPolygons.contains(poly));
		assertTrue(poly.corners.get(2).touchingPolygons.contains(poly));
	}
	
	@Test
	public void ensureCanCachePolygons() {
		Point pt1 = new Point(0, 0);
		Point pt2 = new Point(1, 1);
		Point pt3 = new Point(0, 1);
		Node n1 = util.getNode(pt1);
		Node n2 = util.getNode(pt2);
		Node n3 = util.getNode(pt3);
		
		Polygon original = util.getPolygon(n1, n2, n3);
		Polygon one = util.getPolygon(n1, n2, n3);
		Polygon two = util.getPolygon(n2, n3, n1);
		Polygon three = util.getPolygon(n3, n1, n2);
		
		assertSame(original, one);
		assertSame(original, two);
		assertSame(original, three);
	}
	
	@Test
	public void ensureCanCreateMultiplePolygons() {
		Point pt1 = new Point(0, 1);
		Point pt2 = new Point(0, -1);
		Point pt3 = new Point(1, 0);
		Point pt4 = new Point(-1, 0);
		Node n1 = util.getNode(pt1);
		Node n2 = util.getNode(pt2);
		Node n3 = util.getNode(pt3);
		Node n4 = util.getNode(pt4);
		
		Polygon p1 = util.getPolygon(n1, n2, n3);
		Polygon p2 = util.getPolygon(n1, n2, n4);
		
		// graph tests
		assertThat(graph.polygons.size(), is(equalTo(2)));
		assertThat(graph.arcs.size(), is(equalTo(5)));
		assertThat(graph.nodes.size(), is(equalTo(4)));
		
		// polygon tests
		assertThat(p1.neighbors.size(), is(equalTo(1)));
		assertThat(p2.neighbors.size(), is(equalTo(1)));
		
		// make sure they share one and only one arc
		int numShared = 0;
		for (Arc one : p1.borders) {
			for (Arc two : p2.borders) {
				if (one == two) {
					numShared++;
				}
			}
		}
		assertThat(numShared, is(equalTo(1)));
		
		// node tests
		assertThat(n1.adjacentNodes.size(), is(equalTo(3)));
		assertThat(n2.adjacentNodes.size(), is(equalTo(3)));
		assertThat(n3.adjacentNodes.size(), is(equalTo(2)));
		assertThat(n4.adjacentNodes.size(), is(equalTo(2)));
		
		assertThat(n1.protrudingArcs.size(), is(equalTo(3)));
		assertThat(n2.protrudingArcs.size(), is(equalTo(3)));
		assertThat(n3.protrudingArcs.size(), is(equalTo(2)));
		assertThat(n4.protrudingArcs.size(), is(equalTo(2)));
		
		assertThat(n1.touchingPolygons.size(), is(equalTo(2)));
		assertThat(n2.touchingPolygons.size(), is(equalTo(2)));
		assertThat(n3.touchingPolygons.size(), is(equalTo(1)));
		assertThat(n4.touchingPolygons.size(), is(equalTo(1)));
		
		// arc tests
		Arc a1 = util.getArc(n1, n2);
		Arc a2 = util.getArc(n1, n3);
		Arc a3 = util.getArc(n1, n4);
		Arc a4 = util.getArc(n2, n3);
		Arc a5 = util.getArc(n2, n4);
		
		assertTrue(graph.arcs.contains(a1));
		assertTrue(graph.arcs.contains(a2));
		assertTrue(graph.arcs.contains(a3));
		assertTrue(graph.arcs.contains(a4));
		assertTrue(graph.arcs.contains(a5));
		assertThat(graph.arcs.size(), is(equalTo(5)));
		
		assertThat(a1.borderingPolygons.size(), is(equalTo(2)));
		assertThat(a2.borderingPolygons.size(), is(equalTo(1)));
		assertThat(a3.borderingPolygons.size(), is(equalTo(1)));
		assertThat(a4.borderingPolygons.size(), is(equalTo(1)));
		assertThat(a5.borderingPolygons.size(), is(equalTo(1)));
	}
	
	@Test
	public void ensureCanRemovePolygon() {
		Point pt1 = new Point(0, 1);
		Point pt2 = new Point(0, -1);
		Point pt3 = new Point(1, 0);
		Point pt4 = new Point(-1, 0);
		Node n1 = util.getNode(pt1);
		Node n2 = util.getNode(pt2);
		Node n3 = util.getNode(pt3);
		Node n4 = util.getNode(pt4);
		Polygon p1 = util.getPolygon(n1, n2, n3);
		Polygon p2 = util.getPolygon(n1, n2, n4);
		Arc a1 = util.getArc(n1, n2);
		Arc a2 = util.getArc(n1, n3);
		Arc a3 = util.getArc(n1, n4);
		Arc a4 = util.getArc(n2, n3);
		Arc a5 = util.getArc(n2, n4);		
		
		assertThat(graph.arcs.size(), is(equalTo(5)));
		
		util.removePolygon(p2);
		
		assertThat(graph.arcs.size(), is(equalTo(3)));
		
		// graph tests
		assertThat(graph.polygons.size(), is(equalTo(1)));
		assertThat(graph.arcs.size(), is(equalTo(3)));
		assertThat(graph.nodes.size(), is(equalTo(3)));
		
		// polygon tests
		assertThat(p1.neighbors.size(), is(equalTo(0)));
		assertThat(p2.neighbors.size(), is(equalTo(0)));
		assertThat(p2.borders.size(), is(equalTo(0)));
		assertThat(p2.corners.size(), is(equalTo(0)));
		
		// arc tests
		assertThat(a1.borderingPolygons.size(), is(equalTo(1)));
		assertThat(a2.borderingPolygons.size(), is(equalTo(1)));
		assertThat(a4.borderingPolygons.size(), is(equalTo(1)));
		assertThat(a3.borderingPolygons.size(), is(equalTo(0)));
		assertThat(a5.borderingPolygons.size(), is(equalTo(0)));
		
		assertThat(a1.endPoints.size(), is(equalTo(2)));
		assertThat(a2.endPoints.size(), is(equalTo(2)));
		assertThat(a4.endPoints.size(), is(equalTo(2)));
		assertThat(a3.endPoints.size(), is(equalTo(0)));
		assertThat(a5.endPoints.size(), is(equalTo(0)));
		
		// node tests
		assertThat(n1.touchingPolygons.size(), is(equalTo(1)));
		assertThat(n2.touchingPolygons.size(), is(equalTo(1)));
		assertThat(n3.touchingPolygons.size(), is(equalTo(1)));
		assertThat(n4.touchingPolygons.size(), is(equalTo(0)));
		
		assertThat(n1.protrudingArcs.size(), is(equalTo(2)));
		assertThat(n2.protrudingArcs.size(), is(equalTo(2)));
		assertThat(n3.protrudingArcs.size(), is(equalTo(2)));
		assertThat(n4.protrudingArcs.size(), is(equalTo(0)));
		
		assertThat(n1.adjacentNodes.size(), is(equalTo(2)));
		assertThat(n2.adjacentNodes.size(), is(equalTo(2)));
		assertThat(n3.adjacentNodes.size(), is(equalTo(2)));
		assertThat(n4.adjacentNodes.size(), is(equalTo(0)));
	}
}
