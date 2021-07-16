package oneapp.incture.workbox.demo.chat.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.chat.dto.FavoriteMessageDto;
import oneapp.incture.workbox.demo.chat.entity.FavoriteMessagesDo;

@Repository
//////@Transactional
public class FavoriteMessagesDao extends BaseDao<FavoriteMessagesDo, FavoriteMessageDto>{

	@Override
	protected FavoriteMessagesDo importDto(FavoriteMessageDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		FavoriteMessagesDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getMessageId())
				&& !ServicesUtil.isEmpty(fromDto.getChatId()) && !ServicesUtil.isEmpty(fromDto.getUserId())){
			entity = new FavoriteMessagesDo();
			entity.setChatId(fromDto.getChatId());
			entity.setMessageId(fromDto.getMessageId());
			entity.setUserId(fromDto.getUserId());
		}
		return entity;
	}

	@Override
	protected FavoriteMessageDto exportDto(FavoriteMessagesDo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveOrUpdateFavoriteMessage(FavoriteMessageDto favoriteMessageDto) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(favoriteMessageDto) ) {
				session = this.getSession();
				FavoriteMessageDto currentTask = favoriteMessageDto;
				session.saveOrUpdate(importDto(currentTask));

				session.flush();
				session.clear();
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING FAVORITE MESSAGE] ERROR:"+e.getMessage());
		}
	}

	public void messageUnfavorite(FavoriteMessageDto favoriteMessageDto) {
		
		Query deleteQry = this.getSession().createSQLQuery("DELETE FROM FAVORITE_MESSAGES"
				+ " WHERE MESSAGE_ID = '"+favoriteMessageDto.getMessageId()+"' AND USER_ID = '"+favoriteMessageDto.getUserId()+"'");
		deleteQry.executeUpdate();
	}
}
