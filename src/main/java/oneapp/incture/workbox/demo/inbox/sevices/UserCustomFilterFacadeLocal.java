package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.inbox.dto.UserCustomFilterDto;
import oneapp.incture.workbox.demo.inbox.entity.UserCustomFilter;

public interface UserCustomFilterFacadeLocal {

	public ResponseMessage saveFilterOrView(UserCustomFilter customFilter,Token token);

	public ResponseMessage saveAllFilterOrView(List<UserCustomFilter> customFilter,Token token);

	public List<UserCustomFilterDto> getFilters(Token token);

	public ResponseMessage deleteFilter(UserCustomFilterDto customFilter);

	public ResponseMessage deleteView(UserCustomFilterDto customFilter);

}
