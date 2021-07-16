package oneapp.incture.workbox.demo.sapAriba.service;

import oneapp.incture.workbox.demo.sapAriba.dto.AribaResposeDto;

public interface SapAribaService {

	String createData();

	AribaResposeDto getDetails(String taskId);

	void updateData(String taskId);

}
