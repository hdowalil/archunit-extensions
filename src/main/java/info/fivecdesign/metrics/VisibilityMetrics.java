package info.fivecdesign.metrics;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaModifier;

/**
 * 
 * Visibility of methods in Classes and Classes in Packages
 * @see <a href="https://dzone.com/articles/visibility-metrics-and-the-importance-of-hiding-th">Visibility Metrics and the Importance of Hiding Things</a>
 */
public class VisibilityMetrics {

    private JavaClasses classes = null;
    
	public VisibilityMetrics(JavaClasses classes) {
		super();
		this.classes = classes;
	}

	public double calculateGlobalRelativeVisibilityOfClassMembers() {
		
		double publicMembers = 0;
		double allMembers = 0;
		
		for (JavaClass clazz : classes) {
			for (JavaMember member : clazz.getMembers()) {
				allMembers++;
				if (member.getModifiers().contains(JavaModifier.PUBLIC)) {
					publicMembers++;
				}
			}
		}
		
		return (publicMembers / allMembers) * 100;
	}
	
	public double calculateGlobalRelativeVisibilityOfClassesInPackages() {
		
		double publicClasses = 0;
		
		for (JavaClass clazz : classes) {
			if (clazz.getModifiers().contains(JavaModifier.PUBLIC)) {
				publicClasses++;
			}
		}
		
		return (publicClasses / (double) classes.size()) * 100;
	}
	
}
