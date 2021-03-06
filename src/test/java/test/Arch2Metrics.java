package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import info.fivecdesign.metrics.ImportOptionExcludePackageInfo;
import info.fivecdesign.metrics.JohnLakosMetrics;
import info.fivecdesign.metrics.LackOfCohesion;
import info.fivecdesign.metrics.RelativeCyclicity;
import info.fivecdesign.metrics.VisibilityMetrics;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Arch2Metrics {

	private static final String PACKAGE_ROOT = "test.arch2";
    private JavaClasses classes = null;
    private JohnLakosMetrics metrics = null;
    private RelativeCyclicity cyclicity;
    private VisibilityMetrics visibility;
    private LackOfCohesion cohesion;

    @BeforeAll
    public void importClasses() {
        classes = new ClassFileImporter().withImportOption(new ImportOptionExcludePackageInfo()).importPackages(PACKAGE_ROOT);
        metrics = new JohnLakosMetrics(classes);
        cyclicity = new RelativeCyclicity(classes);
        visibility = new VisibilityMetrics(classes);
        cohesion = new LackOfCohesion();
    }

    @Test
    public void testACD() {
        double acd = metrics.getAverageComponentDependency();
        assertEquals(12.0 / 5.0, acd, 0.01);
   }
	
    @Test
    public void testCCD() {
    	int ccd = metrics.getCumulativeComponentDependency();
    	assertEquals(12,ccd);
    }
    
    @Test
    public void testDependsUpon() {
    	assertEquals(5,metrics.getDependsUpon("test.arch2.C1"));
    	assertEquals(2,metrics.getDependsUpon("test.arch2.C2"));
    	assertEquals(2,metrics.getDependsUpon("test.arch2.C3"));
    	assertEquals(2,metrics.getDependsUpon("test.arch2.C4"));
    	assertEquals(1,metrics.getDependsUpon("test.arch2.C5"));
    	assertEquals(5,metrics.getDependsUpon(metrics.getMaximumDependsUpon()));
    }
	
    @Test
    public void testUsedFrom() {
    	assertEquals(1,metrics.getUsedFrom("test.arch2.C1"));
    	assertEquals(2,metrics.getUsedFrom("test.arch2.C2"));
    	assertEquals(2,metrics.getUsedFrom("test.arch2.C3"));
    	assertEquals(2,metrics.getUsedFrom("test.arch2.C4"));
    	assertEquals(5,metrics.getUsedFrom("test.arch2.C5"));
    	assertEquals(5,metrics.getUsedFrom(metrics.getMaximumUsedFrom()));
    }
    
    @Test
    public void testNCCD() {
    	assertEquals(12.0 / 11.0, metrics.getNormalizedCumulativeComponentDependency(), 0.01);
    }
	
    @Test
    public void testRelativeCyclicityOfClasses() {
    	assertEquals(0.0, cyclicity.calculateRelativeCyclicityOfClasses(), 0.01);
    }
	
    @Test
    public void testRelativeCyclicityOfPackages() {
    	assertEquals(0.0, cyclicity.calculateRelativeCyclicityOfPackages(), 0.01);
    }
    
    @Test
    public void testClassVisibility() {
    	assertEquals(60.0, visibility.calculateGlobalRelativeVisibilityOfClassesInPackages(), 0.01);
    }
	
    @Test
    public void testMemberVisibility() {
    	assertEquals(60.0, visibility.calculateGlobalRelativeVisibilityOfClassMembers(), 0.01);
    }
    
    @Test
    public void testLCOM4() {
    	Map<String, Integer> lcom4Values = cohesion.calculateAllLCOM4Values(classes);
    	assertEquals(1, lcom4Values.get("test.arch2.C1").intValue());
    	assertEquals(1, lcom4Values.get("test.arch2.C2").intValue());
    	assertEquals(1, lcom4Values.get("test.arch2.C3").intValue());
    	assertEquals(2, lcom4Values.get("test.arch2.C4").intValue());
    	assertEquals(1, lcom4Values.get("test.arch2.C5").intValue());
    }
    
    @Test
    public void testLackOfCohesion() {
    	assertEquals(1, cohesion.calculateLackOfCohesion(classes));
    }
}
