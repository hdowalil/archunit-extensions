package info.fivecdesign.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

public class ChidamberKimererMetrics {

	private JavaClasses classes;
	Map<String, Integer> dit;
	Map<String, Integer> noc;

	public ChidamberKimererMetrics(JavaClasses classes) {
		super();
		this.classes = classes;
	}
	
	public Map<String, Integer> calculateAllDepthOfInheritance() {
		if (dit == null) {
			dit = new HashMap<String, Integer>();
			for (JavaClass clazz : classes) {
				if (clazz.getAllSuperClasses() == null) {
					dit.put(clazz.getName(), 0);
				} else {
					dit.put(clazz.getName(), clazz.getAllSuperClasses().size());
				}
			}
		}
		return dit;
	}
	
	public String getMaxDIT() {
		calculateAllDepthOfInheritance();
        return Collections.max(dit.entrySet(), (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> e1.getValue()
                .compareTo(e2.getValue())).getKey();
		
	}
	
	public Integer getDepthOfInheritance(JavaClass clazz) {
		return getDepthOfInheritance(clazz.getName());
	}
	
	public Integer getDepthOfInheritance(String clazzName) {
		calculateAllDepthOfInheritance();
		return dit.get(clazzName);
	}
	
	public Map<String, Integer> caclulateAllNumberOfChildren() {
		if (noc == null) {
			noc = new HashMap<String, Integer>();
			for (JavaClass clazz : classes) {
				if (clazz.getSubClasses() == null) {
					noc.put(clazz.getName(), 0);
				} else {
					noc.put(clazz.getName(), clazz.getSubClasses().size());
				}
			}
		}
		return noc;
	}
	
	public Integer getNumberOfChildren(JavaClass clazz) {
		return getNumberOfChildren(clazz.getName());
	}
	
	public Integer getNumberOfChildren(String clazzName) {
		caclulateAllNumberOfChildren();
		return noc.get(clazzName);
	}
	
	public String getMaxNOC() {
		caclulateAllNumberOfChildren();
        return Collections.max(noc.entrySet(), (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> e1.getValue()
                .compareTo(e2.getValue())).getKey();
	}
}
