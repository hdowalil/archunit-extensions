package info.fivecdesign.metrics;

import java.util.HashSet;
import java.util.Set;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

/**
 * 
 * relative cyclicity of classes
 *
 */
public class RelativeCyclicity {

    private JavaClasses classes = null;

	public RelativeCyclicity(JavaClasses classes) {
		super();
		this.classes = classes;
	}

	public double calculateRelativeCyclicity() {
		
		double cyclic = 0;
		
		for (JavaClass clazz : classes) {
			if (isClassPartOfACycle(clazz)) {
				cyclic++;
			}
		}
		
		return (cyclic / (double) classes.size()) * 100.0;
	}
	
    private boolean isClassPartOfACycle(JavaClass clazz) {
    	
    	// we will try to reach ourself going down all subsequent dependencies
    	// if we cannot, this class is not part of a cycle
    	
    	Set<String> notVisitAgainHaveBeenThere = new HashSet<String>();
    	return findMyselfRecursive(clazz.getDirectDependenciesFromSelf(), notVisitAgainHaveBeenThere, clazz);
    }
    
    private boolean findMyselfRecursive (Set<Dependency> dependencies, Set<String> notVisitAgainHaveBeenThere, JavaClass clazz2Find) {
    	
        for (Dependency classDependency : dependencies) {
        	
            JavaClass accessedClass = classDependency.getTargetClass();
            
            if (accessedClass.equals(clazz2Find)) {
            	// cycle!
            	return true;
            }
            
        	if (!notVisitAgainHaveBeenThere.contains(accessedClass.getName())) {
        		
                notVisitAgainHaveBeenThere.add(accessedClass.getName());
                boolean result = findMyselfRecursive(accessedClass.getDirectDependenciesFromSelf(), notVisitAgainHaveBeenThere, clazz2Find);
                if (result) {
                	// cycle was found in recursive call
                	return true;
                }
        	}
        }

        // no cycle found!
        return false;
    }
}
