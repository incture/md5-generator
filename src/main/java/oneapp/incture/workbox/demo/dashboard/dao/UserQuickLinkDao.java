package oneapp.incture.workbox.demo.dashboard.dao;


import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dashboard.dto.UserQuickLinkDto;
import oneapp.incture.workbox.demo.dashboard.entity.UserQuickLinkDo;

/**
 * @author Neelam Raj
 *
 */
@Repository
public class UserQuickLinkDao extends BaseDao<UserQuickLinkDo, UserQuickLinkDto> {

	@Override
	protected UserQuickLinkDo importDto(UserQuickLinkDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		UserQuickLinkDo entity = new UserQuickLinkDo();
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getQuickLink()))
			entity.setQuickLink(fromDto.getQuickLink());

		return entity;
	}

	@Override
	protected UserQuickLinkDto exportDto(UserQuickLinkDo entity) {

		UserQuickLinkDto dto = new UserQuickLinkDto();
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			dto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getQuickLink()))
			dto.setQuickLink(entity.getQuickLink());

		return dto;
	}

}
