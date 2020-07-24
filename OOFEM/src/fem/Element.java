package fem;

import iceb.jnumerics.*;
import inf.text.ArrayFormat;

public  class Element {
	private double area;
	private double eModulus;
	private int[] dofNumbers=new int[6];
	private Node n1,n2;
	private IMatrix T=new Array2DMatrix(2,6);
	public Element(double e,double a, Node n1,Node n2) {
		area=a;
		eModulus=e;
		this.n1=n1;
		this.n2=n2;
	}
	public void enumerateDOFs() {
		int pos = 0;
        for (int element : n1.getDOFNumbers()) {
        	dofNumbers[pos] = element;
            pos++;
        }
        for (int element : n2.getDOFNumbers()) {
        	dofNumbers[pos] = element;
            pos++;
        }
	} 
	public int[] getDOFNumbers() {
		return dofNumbers;
	} 
	
	public double getLength() {
		double[] x1=n1.getPosition().toArray();
		double[] x2=n2.getPosition().toArray();
		double sum=0;
		for(int i=0;i<x1.length;i++) {
			sum+=Math.pow((x1[i]-x2[i]),2);
		}
		return Math.sqrt(sum);
	} 
	public Node getNode1() {
		return this.n1;
	} 
	public Node getNode2() {
		return this.n2;
	} 
	public double getArea() {
		return this.area;
	} 
	public double getEModulus() {
		return this.eModulus;
	} 
	public void print() {
		System.out.println(ArrayFormat.format(getEModulus())+"\t"+ArrayFormat.format(getArea())+"\t"+ArrayFormat.format(getLength()));
	}
	public IMatrix computeStiffnessMatrix() {
		IMatrix ke=new Array2DMatrix(2,2);
		IMatrix temp=new Array2DMatrix(6,2);
		IMatrix kGlobal=new Array2DMatrix(6,6);
		//Adding the directional cosines in the transformation matrix
		T.addRow(0, 0, getE1());
		T.addRow(1, 3, getE1());
		//Local stiffness Matrix
		double con = (this.eModulus * this.area / getLength());
		ke.add(0, 0, con*1);
		ke.add(0, 1, con*-1);
		ke.add(1, 0, con*-1);
		ke.add(1, 1, con*1);
		//Converting Local Element Stiffness Matrix to global Element Stiffness matrix.
		BLAM . multiply (1.0 , BLAM . TRANSPOSE ,T ,BLAM . NO_TRANSPOSE , ke, 0.0 , temp );
		BLAM . multiply (1.0 , BLAM . NO_TRANSPOSE , temp ,BLAM . NO_TRANSPOSE , T, 0.0 , kGlobal);
		//Returning the global Element Stiffness matrix
		return kGlobal;
	} 
	public Vector3D getE1() {
		double c[]=new double[3];
		for(int i=0;i<c.length;i++) {
			c[i] = (this.n2.getPosition().toArray()[i] - this.n1.getPosition().toArray()[i])/getLength();
		}
		Vector3D v=new Vector3D(c);
		return v;
	}
	public double computeForce() {
		IMatrix temp=new Array2DMatrix(2,1);
		IMatrix u=new Array2DMatrix(6,1);
		u.addColumn(0, 0, this.n1.getDisplacement());
		u.addColumn(3, 0, this.n2.getDisplacement());
		BLAM . multiply (1.0 , BLAM . NO_TRANSPOSE ,T ,BLAM . NO_TRANSPOSE , u, 0.0 , temp );
		double N=(this.eModulus*this.area)*(temp.get(1, 0)-temp.get(0, 0))/this.getLength();
		return N;
	}
}
