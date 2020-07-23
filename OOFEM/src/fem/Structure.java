package fem;

import java.util.*;
import inf.text.ArrayFormat;
import iceb.jnumerics.*;
import iceb.jnumerics.lse.*;

public class Structure {
	private ArrayList<Node> node=new ArrayList<Node>();
	private ArrayList<Element> element=new ArrayList<Element>();
	private IMatrix kGlobal;
	private double[] rGlobal,uGlobal;
	public Node addNode(double x1  , double x2 ,double x3) {
		Node n=new Node(x1,x2,x3);
		node.add(n);
		return n;
	} 
	public Element addElement(double e ,double a ,int id1, int id2) {
		Element elem=new Element(e,a,getNode(id1),getNode(id2));
		element.add(elem);
		return elem;
	} 
	public int getNumberOfNodes() {
		return node.size();
	} 
	public Node getNode(int id) {
		return node.get(id);
	} 
	public int getNumberOfElements() {
		return element.size();
	} 
	public Element getElement(int id) {
		return element.get(id);
	} 
	public void printStructure() {
		int count=0;
		System.out.println("Listing Structure...");
		System.out.println("Nodes");
		System.out.println("idx"+ArrayFormat.fFormat("x1")+ArrayFormat.fFormat("x2")+ArrayFormat.fFormat("x3"));
		for(Node n:node) {
			System.out.print(ArrayFormat.format(count));
			n.print();
			count++;
		}
		System.out.println("Contsraints");
		System.out.println("node"+ArrayFormat.fFormat("u1")+ArrayFormat.fFormat("u2")+ArrayFormat.fFormat("u3"));
		count=0;
		for(Node n:node) {
			if(n.getConstraint()!=null) {
				System.out.print(ArrayFormat.format(count));
				n.getConstraint().print();
			}
			count++;
		}
		System.out.println("Forces");
		count=0;
		System.out.println("node"+ArrayFormat.fFormat("r1")+ArrayFormat.fFormat("r2")+ArrayFormat.fFormat("r3"));
		for(Node n:node) {
			if(n.getForce()!=null) {
				System.out.print(ArrayFormat.format(count));
				n.getForce().print();
			}
			count++;
		}
		System.out.println("Elements");
		System.out.println("idx"+ArrayFormat.fFormat("E")+ArrayFormat.fFormat("A")+"\t"+ArrayFormat.fFormat("length"));
		count=0;
		for(Element e:element) {
			System.out.print(ArrayFormat.format(count));
			e.print();
			count++;
		}
	} 
	public void solve() {
		int NEQ=enumerateDOFs();
		kGlobal=new Array2DMatrix(NEQ,NEQ);
		rGlobal=new double[NEQ];
		uGlobal=new double[NEQ];
		// create the solver object
		ILSESolver solver = new GeneralMatrixLSESolver ();
		QuadraticMatrixInfo aInfo = solver . getAInfo ();
		// Creating K and r with zeros
		kGlobal= solver . getA ();
		rGlobal= new double [NEQ];
		// initialize solver
		aInfo . setSize ( NEQ );
		solver . initialize ();
		// setting entries of matrix and right hand side
		assembleStiffnessMatrix(kGlobal);
		assembleLoadVector(rGlobal);
		// Solver executing for result
		try {
			solver . solve (rGlobal);
		} catch ( SolveFailedException e) {
			System .out . println (" Solve failed : " + e. getMessage ());
		}
		selectDisplacements(rGlobal);
		printResults();
	} 
	private int enumerateDOFs() {
		int max=0;
		int start=0;
		for(Node n:node) {
			start=n.enumerateDOFs(start);
		}
		for(Element e:element) {
			e.enumerateDOFs();
		}
		for(Node n:node) {
			for(int i=0;i<n.getDOFNumbers().length;i++) {
				max=Math.max(max, n.getDOFNumbers()[i]);
			}
		}
		return max+1;
	} 
	private void assembleLoadVector(double[] rGlobal) {
		for(Node n:node) {
			if(n.getForce()!=null) {
				for(int i:n.getDOFNumbers()) {
					if(i!=-1) {
						rGlobal[i]+=n.getForce().getComponent(i);
					}
				}
			}
		}
	} 
	private void assembleStiffnessMatrix(IMatrix kGlobal) {
		for(Element e:element) {
			IMatrix temp=e.computeStiffnessMatrix();
			for(int i:e.getDOFNumbers()) {
				if(i!=-1) {
					for(int j:e.getDOFNumbers()) {
						if(j!=-1) {
							kGlobal.add(i, j, temp.get(i, j));
						}
					}
				}
			}
		}
	} 
	private void selectDisplacements(double[] uGlobal) {
		for(Node n:node) {
			double temp[]=new double[3];
			for(int j=0;j<3;j++) {
				for(int i=0;i<uGlobal.length;i++) {
					if(n.getDOFNumbers()[j]!=-1 && n.getDOFNumbers()[j]==i) {
						temp[j]=temp[j]+uGlobal[i];
					}
				}
			}
			n.setDisplacement(temp);
		}
	} 
	public void printResults() {
		System.out.println("Displacements");
		int count=0;
		System.out.println("node"+ArrayFormat.fFormat("u1")+ArrayFormat.fFormat("u2")+ArrayFormat.fFormat("u3"));
		for(Node n:node) {
			System.out.println(ArrayFormat.format(count)+ArrayFormat.format(n.getDisplacement().toArray()));
			count++;
		}
	} 
}
