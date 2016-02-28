package com.infinity.worldbuilder.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import com.infinity.worldbuilder.model.Node;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.model.Point;

public class PanUnmarshaller {

	public PanGraph unmarshal(String fileName) {
		try {
			FileReader in = new FileReader(fileName);
			return unmarshal(in);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to read " + fileName, e);
		}
	}

	public PanGraph unmarshal(Reader reader) {
		PanUtil util = new PanUtil();
		PanGraph graph = util.getGraph();
		
		LineNumberReader in = new LineNumberReader(reader);
		
		try {
			// how many nodes should we create?
			int numNodes = Integer.parseInt(in.readLine());
			// get all of the nodes
			for (int i = 0; i < numNodes; i++) {
				String line = in.readLine();
				String[] nums = line.split(",");
				float f1 = Float.parseFloat(nums[0]);
				float f2 = Float.parseFloat(nums[1]);
				float f3 = Float.parseFloat(nums[2]);
				
				util.getNode(new Point(f1, f2, f3));
			}
			
			// how many polygons should we create?
			int numPolygons = Integer.parseInt(in.readLine());
			
			// create all of the polygons
			for (int i = 0; i < numPolygons; i++) {
				String line = in.readLine();
				String[] indexes = line.split(",");
				Node[] corners = new Node[indexes.length];
				for (int j = 0; j < indexes.length; j++) {
					corners[j] = graph.nodes.get(Integer.parseInt(indexes[j]));
				}
				util.getPolygon(corners);
			}
			
		} catch (NumberFormatException e) {
			throw new RuntimeException("Unable to parse the stream of data", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to parse the stream of data", e);
		}
		
		return graph;
	}
	
}
