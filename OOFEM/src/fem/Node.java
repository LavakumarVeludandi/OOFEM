package fem;
import iceb.jnumerics.*;
import inf.text.ArrayFormat;

public class Node {
	private int dofNumbers[]=new int[3];
	private Force f;
	private Constraint c;
	private double node[]=new double[3];
	private Vector3D v1;
	private Vector3D v2;
	public Node(double x1,double x2,double x3) {
		this.node[0]=x1;
		this.node[1]=x2;
		this.node[2]=x3;
		
	}
	public void print() {
		System.out.println(ArrayFormat.format(this.node));
	}
	public void setConstraint(Constraint c) {
		this.c=c;
	}
	public Constraint getConstraint() {
		return this.c; 
	}
	public void setForce(Force f) {
		this.f=f;
	}
	public Force getForce() {
		return this.f;
	}
	public int enumerateDOFs(int start) {
		if(this.c==null) {
			for(int i=0;i<dofNumbers.length;i++) {
				dofNumbers[i]=start;
				start++;
				}
			}
		else {
			for(int i=0;i<dofNumbers.length;i++) {
				if(c.isFree(i)) {
					dofNumbers[i]=start;
					start++;
					}
				else {
					dofNumbers[i]=-1;
				}
			}
		}
		return start;
	}	
	public int[] getDOFNumbers() {
		return this.dofNumbers;
	}
	public Vector3D getPosition() {
		v1=new Vector3D(this.node);
		return v1;
	}
	public void setDisplacement(double[] u) {
		v2=new Vector3D(u);
	}
	public Vector3D getDisplacement() {
		return this.v1.add(this.v2);
	}
	
}
