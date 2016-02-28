package com.infinity.worldbuilder;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.infinity.worldbuilder.model.PanGraph;
import com.infinity.worldbuilder.util.ModelUtil;
import com.infinity.worldbuilder.util.PanMarshaller;

public class Playground {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		PanGraph icosahedron = ModelUtil.generateIcosahedron();
		PanMarshaller marshaller = new PanMarshaller();
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < 20; i++) {
			if (i > 0) {
				icosahedron = ModelUtil.generateSubdividedIcosahedron(1, icosahedron);
			}
			long end = System.currentTimeMillis();
			
			System.out.println(i + "\t" + icosahedron.polygons.size() + "\t" + (end - start));
			
			marshaller.marshal(icosahedron, "shape" + i + ".graph");
		}
	}
	
}
