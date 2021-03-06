package info.fivecdesign.metrics;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaConstructor;
import com.tngtech.archunit.core.domain.JavaMember;

/**
 *
 * Lack of Cohesion in given Classes, will return number of distinct subsets in given class.
 * This actually calculates version 4 of LCOM.
 * We also calcualte the same value using the same algorithm for all given classes 
 * 
 */
public class LackOfCohesion {
	
    public LackOfCohesion() {
    	super();
    }
    
	public Map<String, Integer> calculateAllLCOM4Values (JavaClasses classes) {
		
		Map<String, Integer> allLCOM4Values = new HashMap<String, Integer>(); 
		
		for (JavaClass clazz : classes) {
			allLCOM4Values.put(clazz.getName(), calculateLCOM4(clazz));
		}
		
		return allLCOM4Values;
	}
	
	public @Nonnegative int calculateLCOM4(@Nonnull JavaClass clazz) {
		
		DistinctGroups groups = new DistinctGroups();
		
		for (JavaMember member : clazz.getMembers()) {
			
			if (member instanceof JavaConstructor)
				continue;
			
			groups.add(member);
			
			for (JavaAccess<?> access : member.getAccessesToSelf()) {
				
				// access can be JavaConstructorCall, JavaMethodCall, JavaFieldAccess
				// we only care for stuff from the same class
				
				if (access.getOriginOwner().getFullName().equals(clazz.getFullName())) {
					groups.connect(member, access.getOrigin());
					System.out.println(member.toString() + " <- " + access.getOrigin().toString() + " / " + access.getClass().getName());
				}
			}
		}

		return groups.numberOfDistinctGroups();
	}

	public @Nonnegative int calculateLackOfCohesion (@Nonnull JavaClasses classes) {
		
		if (classes == null || classes.size() <= 0) {
			throw new IllegalArgumentException("Can only be calculated for a set of classes");
		}
		
		DistinctGroups groups = new DistinctGroups();
		
		for (JavaClass clazz : classes) {
			groups.add(clazz);
			
			for (Dependency classDependency : clazz.getDirectDependenciesToSelf()) {

				JavaClass accessingClass = classDependency.getOriginClass();
	            if (classes.contain(accessingClass.getName())) {
	            	groups.connect(clazz, accessingClass);
	            }
			}
		}
		
		return groups.numberOfDistinctGroups();
	}
}


