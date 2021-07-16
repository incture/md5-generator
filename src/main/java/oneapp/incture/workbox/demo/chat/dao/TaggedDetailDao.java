package oneapp.incture.workbox.demo.chat.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.chat.dto.TaggedDetailDto;
import oneapp.incture.workbox.demo.chat.entity.TaggedDetailDo;
import oneapp.incture.workbox.demo.chat.entity.TaggedDetailDoPk;

@Repository
public class TaggedDetailDao extends BaseDao<TaggedDetailDo, TaggedDetailDto>{

	@Override
	protected TaggedDetailDo importDto(TaggedDetailDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaggedDetailDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getMessageId())
				&& !ServicesUtil.isEmpty(fromDto.getUserId())){
			entity = new TaggedDetailDo();
			entity.setTaggedDetailDoPk(new TaggedDetailDoPk(fromDto.getMessageId(), fromDto.getUserId()));
			if (!ServicesUtil.isEmpty(fromDto.getChatId()))
				entity.setChatId(fromDto.getChatId());
		}
		return entity;
	}

	@Override
	protected TaggedDetailDto exportDto(TaggedDetailDo entity) {
		TaggedDetailDto taggedDetailDto = new TaggedDetailDto();
		taggedDetailDto.setMessageId(entity.getTaggedDetailDoPk().getMessageId());
		taggedDetailDto.setUserId(entity.getTaggedDetailDoPk().getUserId());
		if (!ServicesUtil.isEmpty(entity.getChatId()))
			taggedDetailDto.setChatId(entity.getChatId());
		return taggedDetailDto;
	}
	
	public void saveOrUpdateTaggedDetail(List<TaggedDetailDto> taggedDetailDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(taggedDetailDtos) && !taggedDetailDtos.isEmpty()) {
				session = this.getSession();
				for (int i = 0; i < taggedDetailDtos.size(); i++) {
					TaggedDetailDto currentTask = taggedDetailDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING TAGGED DETAIL] ERROR:"+e.getMessage());
		}
	}

}
