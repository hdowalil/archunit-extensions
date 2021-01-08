package info.fivecdesign.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistinctGroups {
	
	// every object must be only once in any of the sets!
	private List<Set<Object>> groups;

	public DistinctGroups() {
		super();
		groups = new ArrayList<Set<Object>>();
	}
	
	public void add(Object obj) {
		
		Set<Object> setOfObject = findObjectInSets(obj, groups);
		
		if (setOfObject == null) {
			Set<Object> newSet = new HashSet<Object>();
			newSet.add(obj);
			groups.add(newSet);
		}
	}

	public void connect(Object obj1, Object obj2) {
		
		Set<Object> setOfObject1 = findObjectInSets(obj1, groups);
		Set<Object> setOfObject2 = findObjectInSets(obj2, groups);
		
		if (setOfObject1 == null && setOfObject2 != null) {
			setOfObject2.add(obj1);
			
		} else if (setOfObject1 != null && setOfObject2 == null) {
			setOfObject1.add(obj2);
			
		} else if (setOfObject1 == null && setOfObject2 == null) {
			Set<Object> newSet = new HashSet<Object>();
			newSet.add(obj1);
			newSet.add(obj2);
			groups.add(newSet);
			
		} else if (setOfObject1 != null && setOfObject2 != null) {
			
			if (setOfObject1 != setOfObject2) {
				// we need to merge!
				setOfObject1.addAll(setOfObject2);
				groups.remove(setOfObject2);
			}
		}
	}
	
	public int numberOfDistinctGroups () {
		return groups.size();
	}
	
	private Set<Object> findObjectInSets (Object obj2Find, Collection<Set<Object>> allSets) {
		
		for (Set<Object> singleSet : allSets) {
			if (singleSet.contains(obj2Find)) {
				return singleSet;
			}
		}
		
		return null;
	}

}
