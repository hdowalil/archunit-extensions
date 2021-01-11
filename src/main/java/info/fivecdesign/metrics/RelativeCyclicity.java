package info.fivecdesign.metrics;

import java.util.ArrayList;
import java.util.List;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

/**
 * 
 * Relative Cyclicity of Classes and Packages
 *
 */
public class RelativeCyclicity {

    private JavaClasses classes = null;

	public RelativeCyclicity(JavaClasses classes) {
		super();
		this.classes = classes;
	}
	
	public double calculateRelativeCyclicityOfClasses() {
		
		List<Node> classNodes = buildClassGraph();
		
		double cyclic = 0;
		
		for (Node clazz : classNodes) {
			if (clazz.isPartOfACycle()) {
				cyclic++;
			}
		}
		
		return (cyclic / (double) classes.size()) * 100.0;
	}
	
	public double calculateRelativeCyclicityOfPackages() {
		
		List<Node> pckgNodes = buildPackageGraph();
		
		double cyclic = 0;
		
		for (Node pckg : pckgNodes) {
			if (pckg.isPartOfACycle()) {
				cyclic++;
			}
		}
		
		return (cyclic / (double) pckgNodes.size()) * 100.0;
	}
	
	private List<Node> buildClassGraph() {
		
		List<Node> nodes = new ArrayList<Node>();
		
		for (JavaClass clazz : classes) {
			
			Node node = Node.findNode(nodes, clazz.getName());
			if (node == null) {
				node = new Node(clazz.getName());
				nodes.add(node);
			}
			
	        for (Dependency classDependency : clazz.getDirectDependenciesFromSelf()) {
	        	
	            JavaClass accessedClazz = classDependency.getTargetClass();
	            if (classes.contain(accessedClazz.getName())) {
					Node accessedNode = Node.findNode(nodes, accessedClazz.getName());
					if (accessedNode == null) {
						accessedNode = new Node(accessedClazz.getName());
						nodes.add(accessedNode);
					}
		        	node.connectTo(accessedNode);
	            }
	        }
		}
	
		return nodes;
	}

	private List<Node> buildPackageGraph() {
		
		List<Node> nodes = new ArrayList<Node>();
		
		for (JavaClass clazz : classes) {
			
			Node node = Node.findNode(nodes, clazz.getPackageName());
			if (node == null) {
				node = new Node(clazz.getPackageName());
				nodes.add(node);
			}
			
	        for (Dependency classDependency : clazz.getDirectDependenciesFromSelf()) {
	        	
	            JavaClass accessedClazz = classDependency.getTargetClass();
	            if (classes.contain(accessedClazz.getName()) && !clazz.getPackage().equals(accessedClazz.getPackage())) {
					Node accessedNode = Node.findNode(nodes, accessedClazz.getPackageName());
					if (accessedNode == null) {
						accessedNode = new Node(accessedClazz.getPackageName());
						nodes.add(accessedNode);
					}
		        	node.connectTo(accessedNode);
	            }
	        }
		}
	
		return nodes;
	}

}