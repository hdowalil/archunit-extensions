package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import info.fivecdesign.metrics.ChidamberKimererMetrics;
import info.fivecdesign.metrics.ImportOptionExcludePackageInfo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Arch4Metrics {

	private static final String PACKAGE_ROOT = "test.arch4";
    private JavaClasses classes = null;
    private ChidamberKimererMetrics ck;

    @BeforeAll
    public void importClasses() {
        classes = new ClassFileImporter().withImportOption(new ImportOptionExcludePackageInfo()).importPackages(PACKAGE_ROOT);
        ck = new ChidamberKimererMetrics(classes);
    }

    @Test
    public void testDepthOfInheritance() {
    	assertEquals(Integer.valueOf(3), ck.getDepthOfInheritance(ck.getMaxDIT()));
    }
	
    @Test
    public void testNumberOfChildren() {
    	assertEquals(Integer.valueOf(2), ck.getNumberOfChildren(ck.getMaxNOC()));
    }
	
}
