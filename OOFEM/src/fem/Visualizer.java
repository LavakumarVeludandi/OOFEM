package fem;
import inf.text.ArrayFormat;
import inf.v3d.obj.*;

public class Visualizer {
	private double dispalcementScale;
	private double symbolScale=1;
	private Structure struct;
	public Visualizer(Structure struct){
		this.struct=struct;
	}
	public void drawElements() {
		CylinderSet cs = new CylinderSet();
		for(int i=0;i<struct.getNumberOfElements();i++) {
			cs.addCylinder(struct.getElement(i).getNode1().getPosition().toArray(), 
					struct.getElement(i).getNode2().getPosition().toArray(),
					struct.getElement(i).getArea()*symbolScale);
		}
	} 
	public void drawConstraints() {
		
		for(int i=0;i<struct.getNumberOfNodes();i++) {
			if(struct.getNode(i).getConstraint()!=null) {
				for(int k=0;k<struct.getNode(i).getDOFNumbers().length;k++) {
					if(struct.getNode(i).getDOFNumbers()[k]==-1) {
						
					}
				}
			}
		}
	} 
	public void drawElementForces() {} 
	public void drawDisplacements() {} 
}
