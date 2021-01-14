package info.fivecdesign.metrics;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

/**
 * Relative Cyclicity of Classes and Packages
 *
 */
public class RelativeCyclicity {

    private JavaClasses classes = null;

	public RelativeCyclicity(@Nonnull JavaClasses classes) {
		super();
		this.classes = classes;
	}
	
	public @Nonnegative double calculateRelativeCyclicityOfClasses() {
		
		List<Node> classNodes = buildClassGraph();
		
		double cyclic = 0;
		
		for (Node clazz : classNodes) {
			if (clazz.isPartOfACycle()) {
				cyclic++;
			}
		}
		
		double result = (cyclic / (double) classes.size()) * 100.0;
		
		assert (result >= 0.0 && result <= 100.0);
		
		return result;
	}
	
	public double calculateRelativeCyclicityOfPackages() {
		
		List<Node> pckgNodes = buildPackageGraph();
		
		double cyclic = 0;
		
		for (Node pckg : pckgNodes) {
			if (pckg.isPartOfACycle()) {
				cyclic++;
			}
		}
		
		double result = (cyclic / (double) pckgNodes.size()) * 100.0;
		
		assert (result >= 0.0 && result <= 100.0);
		
		return result;
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