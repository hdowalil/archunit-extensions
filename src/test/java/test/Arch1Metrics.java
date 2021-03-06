package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import info.fivecdesign.metrics.JohnLakosMetrics;
import info.fivecdesign.metrics.RelativeCyclicity;
import info.fivecdesign.metrics.VisibilityMetrics;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Arch1Metrics {

	private static final String PACKAGE_ROOT = "test.arch1";
    private JavaClasses classes = null;
    private JohnLakosMetrics metrics = null;
    private RelativeCyclicity cyclicity;
    private VisibilityMetrics visibility;

    @BeforeAll
    public void importClasses() {
        classes = new ClassFileImporter().importPackages(PACKAGE_ROOT);
        metrics = new JohnLakosMetrics(classes);
        cyclicity = new RelativeCyclicity(classes);
        visibility = new VisibilityMetrics(classes);
    }

    @Test
    public void testACD() {
        double acd = metrics.getAverageComponentDependency();
        assertEquals(15.0 / 5.0, acd, 0.01);
    }
    
    @Test
    public void testCCD() {
    	int ccd = metrics.getCumulativeComponentDependency();
    	assertEquals(15,ccd);
    }
    
    @Test
    public void testDependsUpon() {
    	assertEquals(5,metrics.getDependsUpon("test.arch1.C1"));
    	assertEquals(3,metrics.getDependsUpon("test.arch1.C2"));
    	assertEquals(2,metrics.getDependsUpon("test.arch1.C3"));
    	assertEquals(3,metrics.getDependsUpon("test.arch1.C4"));
    	assertEquals(2,metrics.getDependsUpon("test.arch1.C5"));
    	assertEquals(5,metrics.getDependsUpon(metrics.getMaximumDependsUpon()));
    }
	
    @Test
    public void testUsedFrom() {
    	assertEquals(1,metrics.getUsedFrom("test.arch1.C1"));
    	assertEquals(2,metrics.getUsedFrom("test.arch1.C2"));
    	assertEquals(5,metrics.getUsedFrom("test.arch1.C3"));
    	assertEquals(2,metrics.getUsedFrom("test.arch1.C4"));
    	assertEquals(5,metrics.getUsedFrom("test.arch1.C5"));
    	assertEquals(5,metrics.getUsedFrom(metrics.getMaximumUsedFrom()));
    }
    
    @Test
    public void testNCCD() {
    	assertEquals(15.0 / 11.0, metrics.getNormalizedCumulativeComponentDependency(), 0.01);
    }
    
    @Test
    public void testRelativeCyclicityOfClasses() {
    	assertEquals(40.0, cyclicity.calculateRelativeCyclicityOfClasses(), 0.01);
    }
	
    @Test
    public void testRelativeCyclicityOfPackages() {
    	assertEquals(0.0, cyclicity.calculateRelativeCyclicityOfPackages(), 0.01);
    }
	
    @Test
    public void testClassVisibility() {
    	assertEquals(100.0, visibility.calculateGlobalRelativeVisibilityOfClassesInPackages(), 0.01);
    }
	
    @Test
    public void testMemberVisibility() {
    	assertEquals(100.0, visibility.calculateGlobalRelativeVisibilityOfClassMembers(), 0.01);
    }
	
}
