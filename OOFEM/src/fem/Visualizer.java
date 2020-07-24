package fem;

import iceb.jnumerics.Vector3D;
import inf.v3d.obj.*;
import inf .v3d . view .* ;
public class Visualizer {
	private double dispalcementScale=1e4;
	private double symbolScale=10;
	private Structure struct;
	private Viewer view;
	private double forceSymbolScale;
	private double forceSymbolRadius;
	public Visualizer(Structure struct,Viewer view){
		this.struct=struct;
		this.view=view;
	}
	public void drawNodes() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			Sphere s=new Sphere(struct.getNode(i).getPosition().toArray());
			s.setRadius(0.2);
			s.setColor(0,0,0);
			view.addObject3D(s);
		}
	}
	public void drawElements() {
		CylinderSet cs = new CylinderSet();
		for(int i=0;i<struct.getNumberOfElements();i++) {
			cs.addCylinder(struct.getElement(i).getNode1().getPosition().toArray(), 
					struct.getElement(i).getNode2().getPosition().toArray(),
					struct.getElement(i).getArea()*symbolScale);
			cs.setColor(0, 0, 255);
		}
		view.addObject3D(cs);
	} 
	public void drawConstraints() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			if(struct.getNode(i).getConstraint() != null) {
				for(int j = 0 ; j < 3; j++) {
					if(struct.getNode(i).getDOFNumbers()[j] == -1) {
						Cone c = new Cone(struct.getNode(i).getPosition().toArray()[0],
											struct.getNode(i).getPosition().toArray()[1],
											struct.getNode(i).getPosition().toArray()[2]);
						c.setColor(128,128,128);
						if(j == 0) {
							c.setDirection(1, 0, 0);
							c.translate(-1, 0, 0);
						}
						else if(j == 1) {
							c.setDirection(0, 1, 0);
							c.translate(0, -1, 0);
						}
						else if(j == 2){
							c.setDirection(0, 0, 1);
							c.translate(0, 0, -1);
						}
						view.addObject3D(c);
					}
				}
			}
		}
	}
	public void setForceSymbolScale(double s) {
		this.forceSymbolScale=s;
	}
	public void setForceSymbolRadius(double r) {
		this.forceSymbolRadius=r;
	}
	public void drawElementForces() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			if(struct.getNode(i).getForce()!=null) {
					Arrow a=new Arrow();
					a.setRadius(forceSymbolRadius);
					a.setColor(255,0,0);
					a.setPoint2(struct.getNode(i).getPosition().toArray());
					a.setPoint1(struct.getNode(i).getPosition().toArray()[0]-struct.getNode(i).getForce().getComponent(0)*forceSymbolScale, 
							struct.getNode(i).getPosition().toArray()[1]-struct.getNode(i).getForce().getComponent(1)*forceSymbolScale, 
							struct.getNode(i).getPosition().toArray()[2]-struct.getNode(i).getForce().getComponent(2)*forceSymbolScale);
					view.addObject3D(a);
			}
		}
	} 
	public void drawDisplacements() {
		//Displaced nodes
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			Sphere s=new Sphere();
			double temp[]=new double[3];
			for(int j=0;j<struct.getNode(i).getPosition().getSize();j++) {
				temp[j]=struct.getNode(i).getPosition().toArray()[j]+struct.getNode(i).getDisplacement().toArray()[j]*dispalcementScale;
			}
			s.setCenter(temp);
			s.setRadius(0.2);
			s.setColor(0,60,0);
			view.addObject3D(s);
		}
		//Displaced elements
		CylinderSet cs = new CylinderSet();
		for(int i=0;i<struct.getNumberOfElements();i++) {
			double F=struct.getElement(i).computeForce();
			Vector3D temp1=struct.getElement(i).getNode1().getDisplacement().multiply(dispalcementScale);
			Vector3D temp2=struct.getElement(i).getNode2().getDisplacement().multiply(dispalcementScale);
			Vector3D n1=struct.getElement(i).getNode1().getPosition().add(temp1);
			Vector3D n2=struct.getElement(i).getNode2().getPosition().add(temp2);	
			cs.addCylinder(n1.toArray(), n2.toArray(),struct.getElement(i).getArea()*10);
			cs.setColor(0, 255, 0);
			//Element Normal forces view using polygon set
			Vector3D d=n1.subtract(n2).normalize();
			Vector3D p=d.vectorProduct(n1);
			double []x3=n1.add(F*5e-6, p).toArray();
			double []x4=n2.add(F*5e-6, p).toArray();
			PolygonSet ps=new PolygonSet();
			ps.insertVertex(n1.toArray(), 0);
			ps.insertVertex(n2.toArray(), 1);
			ps.insertVertex(x4, 2);
			ps.insertVertex(x3, 3);
			ps.polygonComplete();
			ps.setVisible(true);
			ps.setColor("red");
			view.addObject3D(ps);
			
		}
		view.addObject3D(cs);
	} 
}
