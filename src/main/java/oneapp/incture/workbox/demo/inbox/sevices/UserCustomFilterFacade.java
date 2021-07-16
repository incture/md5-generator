package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dao.UserCustomFilterDao;
import oneapp.incture.workbox.demo.inbox.dto.UserCustomFilterDto;
import oneapp.incture.workbox.demo.inbox.entity.UserCustomFilter;

@Service("UserCustomFilterFacade")
public class UserCustomFilterFacade implements UserCustomFilterFacadeLocal {

	@Autowired
	UserCustomFilterDao userCustomDao;

	@Override
	public ResponseMessage saveFilterOrView(UserCustomFilter customFilter,Token token) {
		ResponseMessage message = new ResponseMessage();
		String loggedInUser = token.getLogonName();
		try {
			if (!ServicesUtil.isEmpty(customFilter.getUserId())) {
				customFilter.setUserId(customFilter.getUserId());
			} else {
				customFilter.setUserId(loggedInUser);
			}

			if (ServicesUtil.isEmpty(customFilter.getFilterId())) {

				if (userCustomDao.filterNameExists(loggedInUser, customFilter.getFilterName())) {
					message.setMessage("Filter name already exists");
					message.setStatus(PMCConstant.FAILURE);
					message.setStatusCode(PMCConstant.CODE_FAILURE);
				} else {
					return userCustomDao.saveFilterView(customFilter);

				}

			} else {

				if (userCustomDao.filterNameForDifferentIdExists(loggedInUser, customFilter.getFilterId(),
						customFilter.getFilterName())) {
					message.setMessage("Filter name already exists");
					message.setStatus(PMCConstant.FAILURE);
					message.setStatusCode(PMCConstant.CODE_FAILURE);
				} else {

					return userCustomDao.updateFilterView(customFilter);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[WBP-Dev]UserCustomFilterFacade.saveFilterOrView()" + e.getMessage());
			message.setMessage("Filter " + PMCConstant.CREATE_FAILURE);
			message.setStatus(PMCConstant.FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);

		}
		return message;

	}

	@Override
	public List<UserCustomFilterDto> getFilters(Token token) {
		try {
			return userCustomDao.getFilters(token.getLogonName());

		} catch (Exception e) {
			System.err.println("[WBP-Dev]UserCustomFilterFacade.getViews()" + e.getMessage());
			e.printStackTrace();

		}

		return null;
	}

	@Override
	public ResponseMessage deleteFilter(UserCustomFilterDto customFilterDto) {

		try {

			return userCustomDao.deleteFilter(customFilterDto.getFilterId());
		} catch (Exception e) {
			System.err.println("[WBP-Dev]UserCustomFilterFacade.deleteFilter()" + e.getMessage());
		}
		return null;
	}

	@Override
	public ResponseMessage deleteView(UserCustomFilterDto customFilterDto) {
		// TODO Auto-generated method stub

		try {

			return userCustomDao.deleteView(customFilterDto.getFilterId());
		} catch (Exception e) {
			System.err.println("[WBP-Dev]UserCustomFilterFacade.deleteView()" + e.getMessage());
		}
		return null;
	}

	@Override
	public ResponseMessage saveAllFilterOrView(List<UserCustomFilter> customFilter,Token token) {

		return userCustomDao.saveAllFilterView(customFilter,token);
	}

}
