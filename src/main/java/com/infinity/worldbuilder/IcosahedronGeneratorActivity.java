package com.infinity.worldbuilder;

import com.infinity.worldbuilder.actions.Activity;
import com.infinity.worldbuilder.util.ModelUtil;
import com.infinity.worldbuilder.util.PanUtil;

/**
 * Generates a 20 sided icosahedron
 * @author Jeffrey.Richley
 */
public class IcosahedronGeneratorActivity implements Activity {

	public PanUtil performAction(PanUtil currentGraphUtil) {
		return ModelUtil.generateIcosahedron().util;
	}

}
