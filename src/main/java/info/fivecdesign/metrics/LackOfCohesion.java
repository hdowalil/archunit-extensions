package info.fivecdesign.metrics;

import java.util.HashMap;
import java.util.Map;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaConstructor;
import com.tngtech.archunit.core.domain.JavaMember;

/**
 *
 * Lack of Cohesion in given Classes, will return number of distinct subsets
 * plus lack of cohesion in whole set 
 * 
 */
public class LackOfCohesion {
	
    private JavaClasses classes = null;
	
	public LackOfCohesion(JavaClasses classes) {
		super();
		this.classes = classes;
	}

	public Map<String, Integer> calculateAllLCOM4Values () {
		
		Map<String, Integer> allLCOM4Values = new HashMap<String, Integer>(); 
		
		for (JavaClass clazz : classes) {
			allLCOM4Values.put(clazz.getName(), calculateLCOM4(clazz));
		}
		
		return allLCOM4Values;
	}
	
	public int calculateLCOM4(JavaClass clazz) {
		
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

	public int calculateLackOfCohesion () {
		
		DistinctGroups groups = new DistinctGroups();
		
		for (JavaClass clazz : classes) {
			groups.add(clazz);
			
			for (Dependency classDependency : clazz.getDirectDependenciesToSelf()) {

				JavaClass accessingClass = classDependency.getOriginClass();
				groups.connect(clazz, accessingClass);
			}
		}
		
		return groups.numberOfDistinctGroups();
	}
}


