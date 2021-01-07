package info.fivecdesign.metrics;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.*;

public class JohnLakosMetrics {

    private JavaClasses classes = null;

    private Map<String, Integer> dependsUpon = null;
    private Map<String, Integer> usedFrom = null;

    public JohnLakosMetrics(JavaClasses classes) {
        this.classes = classes;
    }

    private int calculateSingleDependsUpon(JavaClass clazz, Set<String> alreadyVisited) {

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

    private int calculateSingleUsedFrom(JavaClass clazz, Set<String> alreadyVisited) {

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

    public int getDependsUpon (String javaClassName) {

        if (dependsUpon == null) {
            calculateAllDependsUpon();
        }

        return dependsUpon.getOrDefault(javaClassName, 0);
    }

    public int getDependsUpon (JavaClass clazz) {

        return getDependsUpon(clazz.getName());
    }

    public int getUsedFrom (String javaClassName) {

        if (usedFrom == null) {
            calculateAllUsedFrom();
        }

        return usedFrom.getOrDefault(javaClassName, 0);
    }

    public int getUsedFrom (JavaClass clazz) {

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

    public int getCumulativeComponentDependency() {

        if (usedFrom == null) {
            calculateAllDependsUpon();
        }

        return usedFrom.values().stream().reduce(0, Integer::sum);

    }

    public double getAverageComponentDependency() {

        return (double) getCumulativeComponentDependency() / (double) dependsUpon.size();
    }

    public double getRelativeAverageComponentDependency() {

        double acd = getAverageComponentDependency();

        return (acd / (double) dependsUpon.size())*100d;
    }

    public double getNormalizedCumulativeComponentDependency () {

        double ccd = (double) getCumulativeComponentDependency();
        double ccd_BinaryTree = (double) getCCDForBalancedBinaryTreeWithNoOfComponents(usedFrom.size());

        return ccd / ccd_BinaryTree;
    }

    /*
     * calculates CCD for a balanced binary tree with a certain number of components
     * a balanced binary tree gets a new level of components when the level above is full
     * the level below can contain double as many components as the level above
     * used-from for level 1 is 1, for level 2 is 2 and so on
     * we add as many used-from values as here are components level per level
     */
    private int getCCDForBalancedBinaryTreeWithNoOfComponents (int noOfComponents) {

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

        return result;
    }

}
