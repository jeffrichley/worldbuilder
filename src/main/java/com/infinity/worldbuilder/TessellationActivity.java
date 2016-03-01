package com.infinity.worldbuilder;

import java.util.Random;

import com.infinity.worldbuilder.actions.Activity;
import com.infinity.worldbuilder.model.Arc;
import com.infinity.worldbuilder.model.Node;
import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.model.Polygon;
import com.infinity.worldbuilder.util.PanUtil;

public class TessellationActivity implements Activity {
	
	private final double percentageToTessellate;

	public TessellationActivity(double percentageToTessellate) {
		this.percentageToTessellate = percentageToTessellate;
	}

	public PanUtil performAction(PanUtil util) {
		PanGraph graph = util.getGraph();
		
		int numArcs = graph.arcs.size();
		int numToTessellate = (int) (numArcs * percentageToTessellate);
		int numTessellated = 0;
		
		Random r = new Random();

		while (numTessellated < numToTessellate) {
			boolean processed = false;
			
			// we need to find an arc, that when tessellated, it will not make any
			// polygons that are less than 5 sided or more than 7 sided
			while (!processed) {
				Arc arc = graph.arcs.get(r.nextInt(numArcs));
				
				Polygon p1 = arc.borderingPolygons.get(0);
				Polygon p2 = arc.borderingPolygons.get(1);
				
				Node p1Node = getOtherNode(p1, arc);
				Node p2Node = getOtherNode(p2, arc);
				Node e1 = arc.endPoints.get(0);
				Node e2 = arc.endPoints.get(1);

				int p1NodeNewCount = p1Node.protrudingArcs.size() + 1;
				int p2NodeNewCount = p2Node.protrudingArcs.size() + 1;
				int endPoint1NewCount = e1.protrudingArcs.size() - 1;
				int endPoint2NewCount = e2.protrudingArcs.size() - 1;
				
				if (p1NodeNewCount <= 7 && p2NodeNewCount <=7 && endPoint1NewCount >=5 && endPoint2NewCount >= 5) {
					// remove the two triangles
					util.removePolygon(p1);
					util.removePolygon(p2);
					
					// create the new triangles, keep in mind the proper winding
					util.getPolygon(p1Node, e1, e2);
					util.getPolygon(p2Node, e2, e1);
					
					processed = true;
				}
			}
			
			numTessellated++;
		}
		
		return util;
	}

	private Node getOtherNode(Polygon p1, Arc tmpArc) {
		Node other = null;
		
		for (Node n : p1.corners) {
			if (!tmpArc.endPoints.contains(n)) {
				other = n;
				break;
			}
		}
		
		return other;
	}

}
