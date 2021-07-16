package oneapp.incture.workbox.demo.dynamicpage.services;

import java.util.List;

import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageResponseDto;

public interface DetailPageFacadeLocal {

	public DynamicPageResponseDto getDetailPage(String taskId, String processName, String catalogue);
}
