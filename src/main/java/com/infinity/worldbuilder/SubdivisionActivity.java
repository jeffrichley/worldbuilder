package com.infinity.worldbuilder;

import com.infinity.worldbuilder.actions.Activity;
import com.infinity.worldbuilder.util.ModelUtil;
import com.infinity.worldbuilder.util.PanUtil;

public class SubdivisionActivity implements Activity {

	private final int numSubdivisions;

	public SubdivisionActivity(int subdivisions) {
		this.numSubdivisions = subdivisions;
	}

	public PanUtil performAction(PanUtil currentGraphUtil) {
		return ModelUtil.generateSubdividedIcosahedron(numSubdivisions, currentGraphUtil.getGraph()).util;
	}

}
