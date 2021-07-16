package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

public class FilterMetadataDto {

	List<FilterLayoutDto> standardFilter;
	List<FilterLayoutDto> customFilter;

	public List<FilterLayoutDto> getStandardFilter() {
		return standardFilter;
	}

	public void setStandardFilter(List<FilterLayoutDto> standardFilter) {
		this.standardFilter = standardFilter;
	}

	public List<FilterLayoutDto> getCustomFilter() {
		return customFilter;
	}

	public void setCustomFilter(List<FilterLayoutDto> customFilter) {
		this.customFilter = customFilter;
	}

	@Override
	public String toString() {
		return "FilterMetadataDto [standardFilter=" + standardFilter + ", customFilter=" + customFilter + "]";
	}

}
