package oneapp.incture.workbox.demo.chat.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.chat.dto.ChatInfoDetailDto;
import oneapp.incture.workbox.demo.chat.entity.ChatInfoDetailDo;

@Repository
//////@Transactional
public class ChatInfoDetailDao extends BaseDao<ChatInfoDetailDo, ChatInfoDetailDto>{

	@Override
	protected ChatInfoDetailDo importDto(ChatInfoDetailDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ChatInfoDetailDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getChatId())){
			entity = new ChatInfoDetailDo();
			entity.setChatId(fromDto.getChatId());
			if (!ServicesUtil.isEmpty(fromDto.getChatName()))
				entity.setChatName(fromDto.getChatName());
			if (!ServicesUtil.isEmpty(fromDto.getChatType()))
				entity.setChatType(fromDto.getChatType());
		}
		return entity;
	}

	@Override
	protected ChatInfoDetailDto exportDto(ChatInfoDetailDo entity) {
		ChatInfoDetailDto chatInfoDetailDto = new ChatInfoDetailDto();
		chatInfoDetailDto.setChatId(entity.getChatId());
		if (!ServicesUtil.isEmpty(entity.getChatName()))
			chatInfoDetailDto.setChatName(entity.getChatName());
		if (!ServicesUtil.isEmpty(entity.getChatType()))
			chatInfoDetailDto.setChatType(entity.getChatType());
		return chatInfoDetailDto;
	}

	public void saveOrUpdateChatInfo(List<ChatInfoDetailDto> chatInfoDetailDtos) {
		for (ChatInfoDetailDto chatInfoDetailDto : chatInfoDetailDtos) {
			try {
				create(chatInfoDetailDto);
			} catch (ExecutionFault | InvalidInputFault | NoResultFault e) {
				e.printStackTrace();
			}
		}
	}

	public void saveOrUpdateChatInfos(List<ChatInfoDetailDto> chatInfoDetailDtos) {
		System.err.println("ChatInfoDetailDao.saveOrUpdateChatInfos()");
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(chatInfoDetailDtos) && !chatInfoDetailDtos.isEmpty()) {
				session = this.getSession();
				for (int i = 0; i < chatInfoDetailDtos.size(); i++) {
					ChatInfoDetailDto currentTask = chatInfoDetailDtos.get(i);
					try {
						session.saveOrUpdate(importDto(currentTask));
					} catch (InvalidInputFault | ExecutionFault | NoResultFault e) {
						e.printStackTrace();
					}
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();

			}
		}catch(Exception e){
			System.err.println("error");
		}
	}
	

	public String getChatName(String chatId) {
		Query fetchNameQry = this.getSession().createSQLQuery("SELECT CHAT_NAME FROM CHAT_INFO_DETAIL WHERE CHAT_ID = '"+chatId+"'");
		String chatName = (String) fetchNameQry.uniqueResult();
		return chatName;
	}

}
