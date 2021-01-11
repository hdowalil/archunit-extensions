package info.fivecdesign.metrics;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public class ImportOptionExcludePackageInfo implements ImportOption {

	@Override
	public boolean includes(Location location) {
		return !location.contains("package-info");
	}
	
	

}
