package com.infinity.worldbuilder.model;

import java.io.Serializable;

/**
 * A pair of x, y coordinates

 * @author jeffreyrichley
 */
public class Point implements Serializable {

	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 7605049661946477302L;

	/**
	 * The x component of this <code>Point</code>.  Yes,
	 * this is public, but only for speed due to the number
	 * of times it is accessed while building the graph.
	 */
	public float x;
	
	/**
	 * The y component of this <code>Point</code>.  Yes,
	 * this is public, but only for speed due to the number
	 * of times it is accessed while building the graph.
	 */
	public float y;
	
	/**
	 * The z component of this <code>Point</code>.  Yes,
	 * this is public, but only for speed due to the number
	 * of times it is accessed while building the graph.
	 */
	public float z;
	
	/**
	 * Constructs a new <code>Point</code>
	 * @param x The x component of this <code>Point</code>
	 * @param y The y component of this <code>Point</code>
	 */
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructs a new <code>Point</code>
	 * @param x The x component of this <code>Point</code>
	 * @param y The y component of this <code>Point</code>
	 * @param z The z component of this <code>Point</code>
	 */
	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point copy() {
		return new Point(x, y, z);
	}

	public Point sub(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		this.z -= p.z;
		return this;
	}
	
	public double dot(Point a) {
		return (this.x * a.x) + (this.y * a.y) + (this.z * a.z);
	}
	
	public Point multiplyScalar(double a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;
		return this;
	}
	
	public Point add(Point a) {
//		if(void 0!==b)return console.warn("THREE.Vector3: .add() now only accepts one argument. Use .addVectors( a, b ) instead."),this.addVectors(a,b);
		this.x += a.x;
		this.y += a.y;
		this.z += a.z;
		return this;
	}

	public Point divideScalar(double a) {
		if (a == 0) {
			this.z = 0;
			this.y = 0; 
			this.x = 0;
		} else {
			a = 1/a;
			this.x *= a;
			this.y *= a;
			this.z *= a;	
		}
		return this;
	}
	
	public String toString() {
		return x + " " + y + " " + z;
	}
}
