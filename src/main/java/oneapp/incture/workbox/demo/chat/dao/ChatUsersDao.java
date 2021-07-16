package oneapp.incture.workbox.demo.chat.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.chat.dto.ChatRequestDto;
import oneapp.incture.workbox.demo.chat.dto.ChatUsersDto;
import oneapp.incture.workbox.demo.chat.entity.ChatUsersDo;
import oneapp.incture.workbox.demo.chat.entity.ChatUsersDoPk;

@Repository
//////@Transactional
public class ChatUsersDao extends BaseDao<ChatUsersDo, ChatUsersDto>{

	@Override
	protected ChatUsersDo importDto(ChatUsersDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ChatUsersDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getChatId())
				&& !ServicesUtil.isEmpty(fromDto.getUserId())){
			entity = new ChatUsersDo();
			entity.setChatUsersDoPk(new ChatUsersDoPk(fromDto.getUserId(), fromDto.getChatId()));
			if (!ServicesUtil.isEmpty(fromDto.getUserName()))
				entity.setUserName(fromDto.getUserName());
			if (!ServicesUtil.isEmpty(fromDto.getLastSeenTime()))
				entity.setLastSeenTime(fromDto.getLastSeenTime());
			if (!ServicesUtil.isEmpty(fromDto.getPinned()))
				entity.setPinned(fromDto.getPinned());
			if (!ServicesUtil.isEmpty(fromDto.getUnseenCount()))
				entity.setUnseenCount(fromDto.getUnseenCount());
			if (!ServicesUtil.isEmpty(fromDto.getChatOpenStatus()))
				entity.setChatOpenStatus(fromDto.getChatOpenStatus());
		}
		return entity;
	}

	@Override
	protected ChatUsersDto exportDto(ChatUsersDo entity) {
		ChatUsersDto chatUsersDto = new ChatUsersDto();
		chatUsersDto.setChatId(entity.getChatUsersDoPk().getChatId());
		chatUsersDto.setUserId(entity.getChatUsersDoPk().getUserId());
		if (!ServicesUtil.isEmpty(entity.getLastSeenTime()))
			chatUsersDto.setLastSeenTime(entity.getLastSeenTime());
		if (!ServicesUtil.isEmpty(entity.getPinned()))
			chatUsersDto.setPinned(entity.getPinned());
		if (!ServicesUtil.isEmpty(entity.getUnseenCount()))
			chatUsersDto.setUnseenCount(entity.getUnseenCount());
		if (!ServicesUtil.isEmpty(entity.getUserName()))
			chatUsersDto.setUserName(entity.getUserName());
		if (!ServicesUtil.isEmpty(entity.getChatOpenStatus()))
			chatUsersDto.setChatOpenStatus(entity.getChatOpenStatus());
		return chatUsersDto;
	}

	public void saveOrUpdateChatUsers(List<ChatUsersDto> chatUsersDtos) {
		try{
			for (ChatUsersDto chatUsersDto : chatUsersDtos) {
				create(chatUsersDto);
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING CHAT USERS] ERROR:"+e.getMessage());
		}
	}

	public void saveOrUpdateChatUser(List<ChatUsersDto> chatUsersDtos) {
		System.err.println("ChatUsersDao.saveOrUpdateChatUser() users to be added : "+chatUsersDtos);
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(chatUsersDtos) && !chatUsersDtos.isEmpty()) {
				session = this.getSession();
				for (int i = 0; i < chatUsersDtos.size(); i++) {
					ChatUsersDto currentTask = chatUsersDtos.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING CHAT USERS] ERROR:"+e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserDetails(ChatRequestDto chatRequestDto) {

		String fetchUsers = "SELECT CHAT_ID,USER_ID,USER_NAME FROM CHAT_USER WHERE CHAT_ID = '"+chatRequestDto.getChatId()+"'";
		Query fetchUserQry = this.getSession().createSQLQuery(fetchUsers);
		List<Object[]> userDetails = fetchUserQry.list();
		System.err.println("ChatUsersDao.getUserDetails() users : "+userDetails);
		return userDetails;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getChatCounts(ChatRequestDto chatRequestDto) {

		String fetchChatCount = "SELECT CHAT_ID,UNSEEN_COUNT FROM CHAT_USER WHERE USER_ID = '"+chatRequestDto.getUserId()+"'";
		Query fetchChatCountQry = this.getSession().createSQLQuery(fetchChatCount);
		List<Object[]> chatCounts = fetchChatCountQry.list();
		return chatCounts;
	}

	public void updateCount(String chatId, String userIds) {
		try{
		String updateCountStr = "UPDATE CHAT_USER SET UNSEEN_COUNT = UNSEEN_COUNT+1 "
				+ "WHERE USER_ID IN ("+userIds.substring(0, userIds.length()-1)+")";
		Query updateCountQry = this.getSession().createSQLQuery(updateCountStr);
		updateCountQry.executeUpdate();
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Update Unseen count error"+e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUserChatDetail(String user, String chatId) {
		String getUserDetailStr = "SELECT USER_ID,USER_NAME,UNSEEN_COUNT,CHAT_ID,CHAT_OPEN_STATUS FROM CHAT_USER "
				+ "WHERE USER_ID IN ("+user.substring(0, user.length()-1)+") AND CHAT_ID = '"+chatId+"'";
		Query getUserDetailQry = this.getSession().createSQLQuery(getUserDetailStr);
		List<Object[]> getUserDetail = getUserDetailQry.list();
		return getUserDetail;
	}

	@SuppressWarnings("unchecked")
	public List<String> getUsersPresent(String chatId) {
		String getUserDetailStr = "SELECT USER_ID FROM CHAT_USER "
										+ "WHERE CHAT_ID = '"+chatId+"'";
		Query getUserDetailQry = this.getSession().createSQLQuery(getUserDetailStr);
		List<String> getUserDetail = (List<String>) getUserDetailQry.list();
		return getUserDetail;
	}
	
	public void updateLastSeen(String chatId,String userId) {
		String updateLastSeenStr = "UPDATE CHAT_USER SET"
				+ " LAST_SEEN_TIME = ADD_SECONDS (CURRENT_TIMESTAMP, 330*60) "
				+ "WHERE USER_ID = '"+userId+"' AND CHAT_ID = '"+chatId+"'";
		Query updateLastSeenQry = this.getSession().createSQLQuery(updateLastSeenStr);
		updateLastSeenQry.executeUpdate();
	}

	public void closeOpenChat(ChatRequestDto chatRequestDto) {
		String updateLastSeenStr = "UPDATE CHAT_USER SET"
				+ " LAST_SEEN_TIME = ADD_SECONDS (CURRENT_TIMESTAMP, 330*60), CHAT_OPEN_STATUS = false "
				+ "WHERE USER_ID = '"+chatRequestDto.getUserId()+"' AND CHAT_ID = '"+chatRequestDto.getChatId()+"'";
		Query updateLastSeenQry = this.getSession().createSQLQuery(updateLastSeenStr);
		updateLastSeenQry.executeUpdate();
	}

	public void pinChat(ChatRequestDto chatRequestDto) {
		String updateLastSeenStr = "UPDATE CHAT_USER SET"
				+ " PINNED = "+chatRequestDto.getIsPinned()+" "
				+ "WHERE USER_ID = '"+chatRequestDto.getUserId()+"' AND CHAT_ID = '"+chatRequestDto.getChatId()+"'";
		Query updateLastSeenQry = this.getSession().createSQLQuery(updateLastSeenStr);
		updateLastSeenQry.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getPinnedChats(String userId) {
		String getChatDetailStr = "SELECT CU.CHAT_ID,CID.CHAT_NAME,CID.CHAT_TYPE ,"
				+ "(SELECT CMD.MESSAGE FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = CU.CHAT_ID AND "
				+ "CMD.SENT_AT = (SELECT MAX(CMD2.SENT_AT) FROM CHAT_MESSAGE_DETAILS CMD2 WHERE "
				+ "CMD2.CHAT_ID = CMD.CHAT_ID GROUP BY CMD2.CHAT_ID) ) as MESSAGE,"
				+ "(SELECT CMD.SENT_AT FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = CU.CHAT_ID AND "
				+ "CMD.SENT_AT = (SELECT MAX(CMD2.SENT_AT) FROM CHAT_MESSAGE_DETAILS CMD2 WHERE "
				+ "CMD2.CHAT_ID = CMD.CHAT_ID GROUP BY CMD2.CHAT_ID) ) as SENT_AT,"
				+ "(SELECT CMD.SENT_BY FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = CU.CHAT_ID AND "
				+ "CMD.SENT_AT = (SELECT MAX(CMD2.SENT_AT) FROM CHAT_MESSAGE_DETAILS CMD2 WHERE "
				+ "CMD2.CHAT_ID = CMD.CHAT_ID GROUP BY CMD2.CHAT_ID) ) as SENT_BY"
				+ " FROM CHAT_USER CU"
				+ " JOIN CHAT_INFO_DETAIL CID ON CID.CHAT_ID = CU.CHAT_ID "
				+ "WHERE USER_ID = '"+userId+"' AND CU.PINNED = TRUE";
		Query getChatDetailQry = this.getSession().createSQLQuery(getChatDetailStr);
		List<Object[]> getChatDetail = getChatDetailQry.list();
		return getChatDetail;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getChatList(ChatRequestDto chatRequestDto) {
		String getChatListStr = "SELECT CU.CHAT_ID,CID.CHAT_TYPE,CID.CHAT_NAME,"
				+ "(SELECT DISTINCT CMD.MESSAGE FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = CU.CHAT_ID AND "
				+ "CMD.SENT_AT = (SELECT MAX(CMD2.SENT_AT) FROM CHAT_MESSAGE_DETAILS CMD2 WHERE "
				+ "CMD2.CHAT_ID = CMD.CHAT_ID GROUP BY CMD2.CHAT_ID) ) as MESSAGE,"
				+ "(SELECT DISTINCT CMD.SENT_AT FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = CU.CHAT_ID AND "
				+ "CMD.SENT_AT = (SELECT MAX(CMD2.SENT_AT) FROM CHAT_MESSAGE_DETAILS CMD2 WHERE "
				+ "CMD2.CHAT_ID = CMD.CHAT_ID GROUP BY CMD2.CHAT_ID) ) as SENT_AT,"
				+ "(SELECT DISTINCT CMD.SENT_BY FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = CU.CHAT_ID AND "
				+ "CMD.SENT_AT = (SELECT MAX(CMD2.SENT_AT) FROM CHAT_MESSAGE_DETAILS CMD2 WHERE "
				+ "CMD2.CHAT_ID = CMD.CHAT_ID GROUP BY CMD2.CHAT_ID) ) as SENT_BY,CU.PINNED  ,"
				+ "(SELECT STRING_AGG(USER_ID,',') FROM CHAT_USER "
				+ "WHERE CHAT_ID = CU.CHAT_ID AND USER_ID <> '"+chatRequestDto.getUserId()+"') AS MEMBERS "
				+ "FROM CHAT_USER CU INNER JOIN CHAT_INFO_DETAIL CID ON CID.CHAT_ID = CU.CHAT_ID "
				+ "WHERE  CU.USER_ID = '"+chatRequestDto.getUserId()+"' AND CID.CHAT_TYPE = '"+chatRequestDto.getChatType()+"'"
				+ " ORDER BY SENT_AT DESC";
		
		Query getChatListQry = this.getSession().createSQLQuery(getChatListStr);
		List<Object[]> getChatListDetail = getChatListQry.list();
		return getChatListDetail;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getChatUser(String loggedInUser) {
		Map<String, String> userChatMap = new HashMap<>();
		String str = "SELECT CID.CHAT_ID,(SELECT USER_ID FROM CHAT_USER WHERE CHAT_ID = CID.CHAT_ID AND USER_ID !='"+loggedInUser+"') "
				+ "FROM CHAT_INFO_DETAIL AS CID "
				+ "INNER JOIN CHAT_USER CU ON CU.CHAT_ID = CID.CHAT_ID "
				+ "WHERE CID.CHAT_TYPE = 'DIRECT' AND CU.USER_ID = '"+loggedInUser+"'";
		Query fetchQry = this.getSession().createSQLQuery(str);
		List<Object[]> fetchResult = fetchQry.list();
		for (Object[] obj : fetchResult) {
			userChatMap.put(obj[1].toString(),obj[0].toString());
		}
		return userChatMap;
	}


}
