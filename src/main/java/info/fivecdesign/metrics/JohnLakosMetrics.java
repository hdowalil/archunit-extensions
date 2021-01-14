package info.fivecdesign.metrics;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author Herbert Dowalil
 *
 * @see <a href="https://www.goodreads.com/book/show/1370617.Large_Scale_C_Software_Design">Large scale C++ Software Design</a>
 * 
 */
public class JohnLakosMetrics {

    private JavaClasses classes = null;

    private Map<String, Integer> dependsUpon = null;
    private Map<String, Integer> usedFrom = null;

    public JohnLakosMetrics(@Nonnull JavaClasses classes) {
        this.classes = classes;
    }

    private @Nonnegative int calculateSingleDependsUpon(@Nonnull JavaClass clazz, @Nonnull Set<String> alreadyVisited) {

        // depends-upon includes the class itself, therefore we start with the class itsfelf
        int count = 1;
        alreadyVisited.add(clazz.getName());

        for (Dependency classDependency : clazz.getDirectDependenciesFromSelf()) {

            JavaClass accessedClass = classDependency.getTargetClass();

            // only dependencies within the analyzed scope are of interest
            if (classes.contain(accessedClass.getName())) {

                if (!alreadyVisited.contains(accessedClass.getName())) {
                    count += calculateSingleDependsUpon(accessedClass, alreadyVisited);
                }
            }
        }
        
        assert (count >= 1);

        return count;
    }

    private void calculateAllDependsUpon() {

        dependsUpon = new HashMap<>();

        for (JavaClass clazz : classes) {

            Set<String> alreadyVisited = new HashSet<>();
            int result = calculateSingleDependsUpon(clazz, alreadyVisited);
            dependsUpon.put(clazz.getName(),result);
        }

    }

    private @Nonnegative int calculateSingleUsedFrom(@Nonnull JavaClass clazz, @Nonnull Set<String> alreadyVisited) {

        // used-from includes the class itself, therefore we start with the class itsfelf
        int count = 1;
        alreadyVisited.add(clazz.getName());

        for (Dependency classDependency : clazz.getDirectDependenciesToSelf()) {

            JavaClass accessingClass = classDependency.getOriginClass();

            // only dependencies within the analyzed scope are of interest
            if (classes.contain(accessingClass.getName())) {

                if (!alreadyVisited.contains(accessingClass.getName())) {
                    count += calculateSingleUsedFrom(accessingClass, alreadyVisited);
                }
            }
        }

        assert (count >= 1);

        return count;
    }

    private void calculateAllUsedFrom() {

        usedFrom = new HashMap<>();

        for (JavaClass clazz : classes) {

            Set<String> alreadyVisited = new HashSet<>();
            int result = calculateSingleUsedFrom(clazz, alreadyVisited);
            usedFrom.put(clazz.getName(),result);
        }

    }

    public @Nonnegative int getDependsUpon (@Nonnull String javaClassName) {

        if (dependsUpon == null) {
            calculateAllDependsUpon();
        }

        return dependsUpon.getOrDefault(javaClassName, 0);
    }

    public @Nonnegative int getDependsUpon (@Nonnull JavaClass clazz) {

    	if (!classes.contains(clazz)) {
    		throw new IllegalArgumentException();
    	}
    	
        return getDependsUpon(clazz.getName());
    }

    public @Nonnegative int getUsedFrom (@Nonnull String javaClassName) {

        if (usedFrom == null) {
            calculateAllUsedFrom();
        }

        return usedFrom.getOrDefault(javaClassName, 0);
    }

    public @Nonnegative int getUsedFrom (@Nonnull JavaClass clazz) {

    	if (!classes.contains(clazz)) {
    		throw new IllegalArgumentException();
    	}
    	
        return getUsedFrom(clazz.getName());
    }

    public String getMaximumDependsUpon() {

        if (dependsUpon == null) {
            calculateAllDependsUpon();
        }

        return Collections.max(dependsUpon.entrySet(), (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> e1.getValue()
                .compareTo(e2.getValue())).getKey();
    }

    public String getMaximumUsedFrom() {

        if (usedFrom == null) {
            calculateAllUsedFrom();
        }

        return Collections.max(usedFrom.entrySet(), (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> e1.getValue()
                .compareTo(e2.getValue())).getKey();
    }

    public @Nonnegative int getCumulativeComponentDependency() {

        if (dependsUpon == null) {
            calculateAllDependsUpon();
        }

        return dependsUpon.values().stream().reduce(0, Integer::sum);

    }

    public @Nonnegative double getAverageComponentDependency() {

        return (double) getCumulativeComponentDependency() / (double) dependsUpon.size();
    }

    public @Nonnegative double getRelativeAverageComponentDependency() {

        double acd = getAverageComponentDependency();

        return (acd / (double) dependsUpon.size())*100d;
    }

    public @Nonnegative double getNormalizedCumulativeComponentDependency () {

        double ccd = (double) getCumulativeComponentDependency();
        double ccd_BinaryTree = (double) getCCDForBalancedBinaryTreeWithNoOfComponents(dependsUpon.size());

        return ccd / ccd_BinaryTree;
    }

    /*
     * calculates CCD for a balanced binary tree with a certain number of components
     * a balanced binary tree gets a new level of components when the level above is full
     * the level below can contain double as many components as the level above
     * used-from for level 1 is 1, for level 2 is 2 and so on
     * we add as many used-from values as here are components level per level
     */
    private @Nonnegative int getCCDForBalancedBinaryTreeWithNoOfComponents (@Nonnegative int noOfComponents) {

    	if (noOfComponents <= 0) {
    		throw new IllegalArgumentException();
    	}
        
    	int result = 0;

        int componentsPerLevel = 1;
        int currentComponentOnLevel = 0;
        int usedFromWeightOnLevel = 1;
        int alreadyCounted = 0;

        while (alreadyCounted < noOfComponents) {

            result += usedFromWeightOnLevel;

            currentComponentOnLevel++;
            alreadyCounted++;

            if (currentComponentOnLevel >= componentsPerLevel) {
                // lets move one level lower in our binary tree
                currentComponentOnLevel = 0;
                usedFromWeightOnLevel++;
                componentsPerLevel *= 2;
            }

        }
        
        assert(result > 0);

        return result;
    }

}
