package info.fivecdesign.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
 * contains a list of object-sets
 * objects in the same set are connected to each other
 * every object must only appear in max. one set
 */
class DistinctGroups {
	
	// every object must be only once in any of the sets!
	private List<Set<Object>> groups;

	DistinctGroups() {
		super();
		groups = new ArrayList<Set<Object>>();
	}
	
	void add(@Nonnull Object obj) {
		
		Set<Object> setOfObject = findObjectInSets(obj, groups);
		
		if (setOfObject == null) {
			Set<Object> newSet = new HashSet<Object>();
			newSet.add(obj);
			groups.add(newSet);
		}
	}

	void connect(@Nonnull Object obj1, @Nonnull Object obj2) {
		
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
	
	int numberOfDistinctGroups () {
		return groups.size();
	}
	
	@Nullable
	private Set<Object> findObjectInSets (@Nonnull Object obj2Find, @Nonnull Collection<Set<Object>> allSets) {
		
		for (Set<Object> singleSet : allSets) {
			if (singleSet.contains(obj2Find)) {
				return singleSet;
			}
		}
		
		return null;
	}

}
