package fem;

import java.util.*;
import inf.text.ArrayFormat;

public class Structure {
	private ArrayList<Node> node=new ArrayList<Node>();
	private ArrayList<Element> element=new ArrayList<Element>();
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
	//public void solve() {} 
	//private int enumerateDOFs() {} 
	//private void assembleLoadVector(double[] rGlobal) {} 
	//private void assembleStiffnessMatrix(IMatrix kGlobal) {} 
	//private void selectDisplacements(double[] uGlobal) {} 
	//public void printResults() {} 
}
