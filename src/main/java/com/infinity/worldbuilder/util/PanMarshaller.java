package com.infinity.worldbuilder.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import com.infinity.worldbuilder.model.Node;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.model.Polygon;

public class PanMarshaller {
	
	public void marshal(PanGraph graph, String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			marshal(graph, out);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to write to " + fileName, e);
		}
	}

	public void marshal(PanGraph graph, OutputStream out) {
		StringBuilder buff = new StringBuilder();
		
		// save the nodes
		// first write how many nodes there are
		buff.append(graph.nodes.size()).append("\n");
		for (Node n : graph.nodes) {
			buff.append(n.point.x).append(",").append(n.point.y).append(",").append(n.point.z).append("\n");
		}
		
		// save the polygons
		// write how many polygons there are
		buff.append(graph.polygons.size()).append("\n");
		for (Polygon p : graph.polygons) {
			Iterator<Node> nodeIterator = p.corners.iterator();
			while (nodeIterator.hasNext()) {
				Node n = nodeIterator.next();
				buff.append(graph.nodes.indexOf(n));
				if (nodeIterator.hasNext()) {
					buff.append(",");
				}
			}
			buff.append("\n");
		}
		
		try {
			out.write(buff.toString().getBytes());
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException("Unable to save graph", e);
		}
	}
	
	public static void main(String[] args) {
		PanMarshaller m = new PanMarshaller();
		PanGraph shape = ModelUtil.generateIcosahedron();
		
		m.marshal(shape, "shape.graph");
	}
}
