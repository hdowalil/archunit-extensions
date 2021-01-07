package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import info.fivecdesign.metrics.JohnLakosMetrics;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Arch2Metrics {

	private static final String PACKAGE_ROOT = "test.arch2";
    private JavaClasses classes = null;
    private JohnLakosMetrics metrics = null;

    @BeforeAll
    public void importClasses() {
        classes = new ClassFileImporter().importPackages(PACKAGE_ROOT);
        metrics = new JohnLakosMetrics(classes);
    }

    @Test
    public void testACD() {
        double acd = metrics.getAverageComponentDependency();
        assertEquals(12.0 / 5.0, acd, 0.01);
   }
	
}
