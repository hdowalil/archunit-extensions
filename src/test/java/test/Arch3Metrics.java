package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import info.fivecdesign.metrics.ImportOptionExcludePackageInfo;
import info.fivecdesign.metrics.RelativeCyclicity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Arch3Metrics {

	private static final String PACKAGE_ROOT = "test.arch3";
    private JavaClasses classes = null;
    private RelativeCyclicity cyclicity;

    @BeforeAll
    public void importClasses() {
        classes = new ClassFileImporter().withImportOption(new ImportOptionExcludePackageInfo()).importPackages(PACKAGE_ROOT);
        cyclicity = new RelativeCyclicity(classes);
    }

    @Test
    public void testRelativeCyclicityOfClasses() {
    	assertEquals(40.0, cyclicity.calculateRelativeCyclicityOfClasses(), 0.01);
    }
	
    @Test
    public void testRelativeCyclicityOfPackages() {
    	assertEquals(66.66, cyclicity.calculateRelativeCyclicityOfPackages(), 0.01);
    }
    
}
