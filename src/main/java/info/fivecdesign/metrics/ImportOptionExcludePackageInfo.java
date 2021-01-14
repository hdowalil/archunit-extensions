package info.fivecdesign.metrics;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

/**
 * @author Herbert Dowalil
 * 
 * It does not make much sense to include the package-infos when calculating structural metrics.
 * By using this ArchUnit Import Option you can exclude them from being parsed
 *
 */
public class ImportOptionExcludePackageInfo implements ImportOption {

	@Override
	public boolean includes(Location location) {
		return !location.contains("package-info");
	}
	
}
