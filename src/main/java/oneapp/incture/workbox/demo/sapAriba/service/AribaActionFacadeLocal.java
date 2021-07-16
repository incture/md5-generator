package oneapp.incture.workbox.demo.sapAriba.service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.sapAriba.dto.AribaActionDto;

public interface AribaActionFacadeLocal {

	public ResponseMessage performAction(AribaActionDto actionDto);
}
