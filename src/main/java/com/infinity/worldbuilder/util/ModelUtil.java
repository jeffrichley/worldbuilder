package com.infinity.worldbuilder.util;

import java.util.ArrayList;
import java.util.List;

import com.infinity.worldbuilder.model.Node;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.model.Point;
import com.infinity.worldbuilder.model.Polygon;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class ModelUtil {
	
	public static PanGraph generateSubdividedIcosahedron(int degree, PanGraph icosahedron) {
		PanUtil util = new PanUtil();
		PanGraph graph = util.getGraph();
		
		for (Polygon polygon : icosahedron.polygons) {
			Point p1 = polygon.corners.get(0).point;
			Point p2 = polygon.corners.get(1).point;
			Point p3 = polygon.corners.get(2).point;
			subdivideTriangle(util, p1, p2, p3, degree, 0);
		}
		
		return graph;
	}
	

	private static void subdivideTriangle(PanUtil util, Point p1, Point p2, Point p3, int degree, int currentIteration) {
		// no more subdividing
		if (degree == currentIteration) {
			Node n1 = util.getNode(p1);
			Node n2 = util.getNode(p2);
			Node n3 = util.getNode(p3);
			util.getPolygon(n1, n2, n3);
		} else {
			// need to subdivide
			Point between12 = slerp(p1, p2, .5);
			Point between23 = slerp(p2, p3, .5);
			Point between31 = slerp(p3, p1, .5);
			
//			util.getPolygon(n1, between12, between31); // good
			int nextIteration = currentIteration + 1;
			subdivideTriangle(util, p1, between12, between31, degree, nextIteration);
//			util.getPolygon(between12, n2, between23); // good
			subdivideTriangle(util, between12, p2, between23, degree, nextIteration);
//			util.getPolygon(between31, between23, n3); // good
			subdivideTriangle(util, between31, between23, p3, degree, nextIteration);
//			util.getPolygon(between31, between12, between23); // good
			subdivideTriangle(util, between31, between12, between23, degree, nextIteration);
		}
	}


	public static PanGraph generateSubdividedIcosahedronTwo(int degree, PanGraph icosahedron) {
		PanUtil util = new PanUtil();
		
		// copy the original graph
		for (Polygon p : icosahedron.polygons) {
			Node n1 = util.getNode(p.corners.get(0).point);
			Node n2 = util.getNode(p.corners.get(1).point);
			Node n3 = util.getNode(p.corners.get(2).point);
			util.getPolygon(n1, n2, n3);
		}
		
		for (int i = 1; i < degree; i++) {
			List<Polygon> polygons = new ArrayList<Polygon>(util.getGraph().polygons);
			for (Polygon p : polygons) {
				Node n1 = util.getNode(p.corners.get(0).point);
				Node n2 = util.getNode(p.corners.get(1).point);
				Node n3 = util.getNode(p.corners.get(2).point);
				
				Node between12 = util.getNode(slerp(n1.point, n2.point, .5));
				Node between23 = util.getNode(slerp(n2.point, n3.point, .5));
				Node between31 = util.getNode(slerp(n3.point, n1.point, .5));
				
				util.removePolygon(p);
				
				util.getPolygon(n1, between12, between31); // good
				util.getPolygon(between12, n2, between23); // good
				util.getPolygon(between31, between23, n3); // good
				util.getPolygon(between31, between12, between23); // good
			}
		}
		
		return util.getGraph();
	}
	
	public static PanGraph generateSubdividedIcosahedronOld(int degree, PanGraph icosahedron) {
		PanGraph answer = icosahedron;
		
		for (int i = 1; i < degree; i++) {
			PanUtil util = new PanUtil();
			
			for (Polygon p : answer.polygons) {
				Node n1 = util.getNode(p.corners.get(0).point);
				Node n2 = util.getNode(p.corners.get(1).point);
				Node n3 = util.getNode(p.corners.get(2).point);
				
				Node between12 = util.getNode(slerp(n1.point, n2.point, .5));
				Node between23 = util.getNode(slerp(n2.point, n3.point, .5));
				Node between31 = util.getNode(slerp(n3.point, n1.point, .5));
				
//				print(n1);
//				print(n2);
//				print(n3);
//				System.out.println("--------");
//				print(between12);
//				print(between23);
//				print(between31);

				util.getPolygon(n1, between12, between31); // good
				util.getPolygon(between12, n2, between23); // good
				util.getPolygon(between31, between23, n3); // good
				util.getPolygon(between31, between12, between23); // good
			}
			
			answer = util.getGraph();
		}
		
//		System.out.println("****** " + answer.polygons.size());
		return answer;
	}

	private static void print(Node n) {
		System.out.println(n.point.x + " " + n.point.y + " " + n.point.z);
		if (n.point.x == 0 && n.point.y == 0 && n.point.z == 0) {
			System.out.println("found one");
		}
	}

	private static Point slerp(Point p0, Point p1, double t) {
		double omega = Math.acos(p0.dot(p1));
		return p0.copy().multiplyScalar(Math.sin((1 - t) * omega)).add(p1.copy().multiplyScalar(Math.sin(t * omega))).divideScalar(Math.sin(omega));
	}
	
	public static PanGraph generateIcosahedron() {
		PanUtil util = new PanUtil();
		
		float phi = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);
		float du = (float) (1.0 / Math.sqrt(phi * phi + 1.0));
		float dv = phi * du;
		
		// create the 12 nodes
		Node n0 = util.getNode(new Point(0, +dv, +du));
		Node n1 = util.getNode(new Point(0, +dv, -1*du));
		Node n2 = util.getNode(new Point(0, -1*dv, +du));
		Node n3 = util.getNode(new Point(0, -1*dv, -1*du));
		Node n4 = util.getNode(new Point(+du, 0, +dv));
		Node n5 = util.getNode(new Point(-1*du, 0, +dv));
		Node n6 = util.getNode(new Point(+du, 0, -1*dv));
		Node n7 = util.getNode(new Point(-1*du, 0, -1*dv));
		Node n8 = util.getNode(new Point(+dv, +du, 0));
		Node n9 = util.getNode(new Point(+dv, -1*du, 0));
		Node n10 = util.getNode(new Point(-1*dv, +du, 0));
		Node n11 = util.getNode(new Point(-1*dv, -1*du, 0));
		
		// add the 20 sides
		util.getPolygon(n0, n1, n8);
		util.getPolygon(n0,  n1,  n8);
		util.getPolygon(n0,  n4,  n5);
		util.getPolygon(n0,  n5, n10);
		util.getPolygon(n0,  n8,  n4);
		util.getPolygon(n0, n10,  n1);
		util.getPolygon(n1,  n6,  n8);
		util.getPolygon(n1,  n7,  n6);
		util.getPolygon(n1, n10,  n7);
		util.getPolygon(n2,  n3, n11);
		util.getPolygon(n2,  n4,  n9);
		util.getPolygon(n2,  n5,  n4);
		util.getPolygon(n2,  n9,  n3);
		util.getPolygon(n2, n11,  n5);
		util.getPolygon(n3,  n6,  n7);
		util.getPolygon(n3,  n7, n11);
		util.getPolygon(n3,  n9,  n6);
		util.getPolygon(n4,  n8,  n9);
		util.getPolygon(n5, n11, n10);
		util.getPolygon(n6,  n9,  n8);
		util.getPolygon(n7, n10, n11);
		
		return util.getGraph();
	}

	public static Mesh graphToMesth(PanGraph graph) {
		Mesh mesh = new Mesh();
		
		Vector3f[] vertices = new Vector3f[graph.nodes.size()];
		int verticeCount = 0;
		for (Node node : graph.nodes) {
			Point p = node.point;
			vertices[verticeCount++] = new Vector3f(p.x, p.y, p.z);
		}
		
		int[] indexes = new int[graph.polygons.size() * 3];
		int nodeCount = 0;
		for (Polygon p : graph.polygons) {
//			for (int i = p.corners.size() - 1; i >= 0; i--) {
//				Node node = p.corners.get(i);
			for (Node node : p.corners) {
				indexes[nodeCount++] = graph.nodes.indexOf(node);
			}
		}
		
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
		mesh.updateBound();
		
		return mesh;
	}

}
