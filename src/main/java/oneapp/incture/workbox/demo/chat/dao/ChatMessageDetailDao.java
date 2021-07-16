package oneapp.incture.workbox.demo.chat.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.chat.dto.ChatMessageDetailDto;
import oneapp.incture.workbox.demo.chat.dto.ChatRequestDto;
import oneapp.incture.workbox.demo.chat.entity.ChatMessageDetailDo;
import oneapp.incture.workbox.demo.chat.util.ChatConstant;

@Repository
public class ChatMessageDetailDao extends BaseDao<ChatMessageDetailDo, ChatMessageDetailDto> {

	@Override
	protected ChatMessageDetailDo importDto(ChatMessageDetailDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ChatMessageDetailDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getMessageId())) {
			entity = new ChatMessageDetailDo();
			entity.setMessageId(fromDto.getMessageId());
			if (!ServicesUtil.isEmpty(fromDto.getAttachmentId()))
				entity.setAttachmentId(fromDto.getAttachmentId());
			if (!ServicesUtil.isEmpty(fromDto.getChatId()))
				entity.setChatId(fromDto.getChatId());
			if (!ServicesUtil.isEmpty(fromDto.getMessage()))
				entity.setMessage(fromDto.getMessage());
			if (!ServicesUtil.isEmpty(fromDto.getSentBy()))
				entity.setSentBy(fromDto.getSentBy());
			if (!ServicesUtil.isEmpty(fromDto.getFavorite()))
				entity.setFavorite(fromDto.getFavorite());
			if (!ServicesUtil.isEmpty(fromDto.getSentAt()))
				entity.setSentAt(fromDto.getSentAt());
		}
		return entity;
	}

	@Override
	protected ChatMessageDetailDto exportDto(ChatMessageDetailDo entity) {
		ChatMessageDetailDto chatMessageDetailDto = new ChatMessageDetailDto();
		chatMessageDetailDto.setMessageId(entity.getMessageId());
		if (!ServicesUtil.isEmpty(entity.getAttachmentId()))
			chatMessageDetailDto.setAttachmentId(entity.getAttachmentId());
		if (!ServicesUtil.isEmpty(entity.getChatId()))
			chatMessageDetailDto.setChatId(entity.getChatId());
		if (!ServicesUtil.isEmpty(entity.getMessage()))
			chatMessageDetailDto.setMessage(entity.getMessage());
		if (!ServicesUtil.isEmpty(entity.getSentBy()))
			chatMessageDetailDto.setSentBy(entity.getSentBy());
		if (!ServicesUtil.isEmpty(entity.getFavorite()))
			chatMessageDetailDto.setFavorite(entity.getFavorite());
		if (!ServicesUtil.isEmpty(entity.getSentAt()))
			chatMessageDetailDto.setSentAt(entity.getSentAt());
		return chatMessageDetailDto;
	}

	public void saveOrUpdateChatMessage(ChatMessageDetailDto chatMessageDetailDto) {
		Session session = null;
		try {
			if (!ServicesUtil.isEmpty(chatMessageDetailDto)) {
				session = this.getSession();
				ChatMessageDetailDto currentTask = chatMessageDetailDto;
				session.saveOrUpdate(importDto(currentTask));

				session.flush();
				session.clear();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING CHAT MESSAGE] ERROR:" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getMessages(ChatRequestDto chatRequestDto) {

		try {
			if (ServicesUtil.isEmpty(chatRequestDto.getPage()))
				chatRequestDto.setPage(1);

			String fetchMessagesStr = "SELECT CMD.MESSAGE_ID,CMD.MESSAGE,CMD.SENT_BY,CMD.SENT_AT";

			fetchMessagesStr = fetchMessagesStr + ",CASE WHEN (SELECT TRUE FROM FAVORITE_MESSAGES "
					+ "WHERE MESSAGE_ID= CMD.MESSAGE_ID AND USER_ID = '" + chatRequestDto.getUserId()
					+ "' )=TRUE THEN TRUE " + "ELSE FALSE END AS FAVORITE";

			
			String attachmentStr = ",(SELECT STRING_AGG(DOCUMENT_ID || '$' || DOCUMENT_TYPE || '$' || DOCUMENT_NAME || '$' || DOCUMENT_SIZE,',')"
					+ " FROM ATTACHMENTS WHERE ATTACHMENT_ID = CMD.ATTACHMENT_ID) AS ATTACHMENTS";

			String taggedStr = ", (SELECT STRING_AGG(USER_ID,',') FROM TAGGED_DETAIL WHERE MESSAGE_ID = CMD.MESSAGE_ID) as TAGGED_DETAIL ";

			String sentByNameStr = ",(SELECT USER_FIRST_NAME||' '||USER_LAST_NAME FROM USER_IDP_MAPPING WHERE USER_ID = CMD.SENT_BY) ";
			String whereClause = "FROM CHAT_MESSAGE_DETAILS CMD WHERE CMD.CHAT_ID = '" + chatRequestDto.getChatId()
					+ "' ORDER BY CMD.SENT_AT DESC ";
			String limit = "LIMIT " + ChatConstant.CHAT_LIMIT + " OFFSET "
					+ ((chatRequestDto.getPage() - 1) * ChatConstant.CHAT_LIMIT);

			Query fetchMessagesQry = this.getSession()
					.createSQLQuery(fetchMessagesStr + attachmentStr + taggedStr + sentByNameStr + whereClause + limit);
			List<Object[]> fetchMessageResult = fetchMessagesQry.list();
			return fetchMessageResult;

		} catch (Exception e) {
			System.err.println("[WBP-Dev]WOKBOX-NEW CHAT:RETRIVING MESSAGES ERROR" + e.getMessage());
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAllFavorite(String userId) {
		String attachmentStr = "";
		String taggedStr = "";

		String fetchMessages = "select fm.chat_id,fm.message_id,cmd.message,cmd.sent_by,cmd.sent_at,"
				+ "cid.chat_name,chat_type ";

		String whereClause = "from favorite_messages fm "
				+ "inner join chat_message_details as cmd on cmd.message_id = fm.message_id "
				+ "inner Join chat_info_detail cid on cid.chat_id = fm.chat_id " + "where fm.user_id = '" + userId
				+ "' ORDER BY CMD.SENT_AT";
		attachmentStr = ",(SELECT STRING_AGG(DOCUMENT_ID || '$' || DOCUMENT_TYPE ||'$' || DOCUMENT_NAME,',')"
				+ " FROM ATTACHMENTS WHERE ATTACHMENT_ID = CMD.ATTACHMENT_ID) AS ATTACHMENTS";

		taggedStr = ", (SELECT STRING_AGG(USER_ID,',') FROM TAGGED_DETAIL WHERE MESSAGE_ID = CMD.MESSAGE_ID) as TAGGED_DETAIL ";

		String sentByNameQry = ",(select USER_FIRST_NAME || ' ' || USER_LAST_NAME "
				+ "from USER_IDP_MAPPING where user_id = cmd.sent_by) as sentByName ";
		Query fetchQry = this.getSession()
				.createSQLQuery(fetchMessages + attachmentStr + taggedStr + sentByNameQry + whereClause);
		List<Object[]> chatDetail = fetchQry.list();
		return chatDetail;
	}

	public Integer getTotalMessageCount(ChatRequestDto chatRequestDto) {
		Integer count = 0;
		try {
			String countQry = "SELECT COUNT(*) FROM CHAT_MESSAGE_DETAILS WHERE CHAT_ID = '" + chatRequestDto.getChatId()
					+ "'";
			Query fetchQry = this.getSession().createSQLQuery(countQry);
			count = ((BigInteger) fetchQry.uniqueResult()).intValue();
		} catch (Exception e) {
			System.err.println("[WBP-Dev] GET CHAT COUNT" + e.getMessage());
		}
		return count;
	}

}
