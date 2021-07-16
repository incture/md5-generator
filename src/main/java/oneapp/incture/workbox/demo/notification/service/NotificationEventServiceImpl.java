package oneapp.incture.workbox.demo.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;
import oneapp.incture.workbox.demo.notification.dao.ApplicationUpdatesDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationEventsDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationProfileSettingDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationSettingDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationUserConfigDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationViewSettingDao;
import oneapp.incture.workbox.demo.workflow.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.workflow.dto.CrossConstantDto;
import oneapp.incture.workbox.demo.notification.dao.NotificationEventChannelDao;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;
import oneapp.incture.workbox.demo.notification.util.TimeZoneConvertion;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.notification.dto.ApplicationUpdatesDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationAllSettingResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationEventChannelDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationProfileResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationProfileSettingDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationSettingDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationSettingResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewDetailRequestDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewDetailResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewSettingsDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationEventDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


@Service
//@Transactional
public class NotificationEventServiceImpl implements NotificationEventService {

	@Autowired
	NotificationEventsDao notificationDao;

	@Autowired
	NotificationUserConfigDao userConfigDao;

	@Autowired
	NotificationViewSettingDao viewSettingDao;
	@Autowired
	NotificationProfileSettingDao notificationProfileSettingDao;
	@Autowired
	NotificationSettingDao notificationSettingDao;
	@Autowired
	ApplicationUpdatesDao applicationUpdatesDao;
	@Autowired
	CrossConstantDao crossConstantDao;
	@Autowired
	NotificationEventChannelDao notificationEventChannelDao;

	@Autowired
	private TimeZoneConvertion timeZoneConvertion;

	@Override
	public NotificationViewResponseDto getViewList(String userId, String isDefault,Token token) {
		System.err.println("NotificationEventServiceImpl.getViewList() userId" + userId);
		ResponseMessage message = new ResponseMessage();
		message.setStatus(PMCConstant.SUCCESS);
		message.setStatusCode(PMCConstant.CODE_SUCCESS);
		NotificationViewResponseDto responseDto = new NotificationViewResponseDto();
		List<NotificationViewSettingsDto> viewDtos = null;
		try {

			if (ServicesUtil.isEmpty(userId)) {
				//userId = UserManagementUtil.getLoggedInUser().getName();
				
				// trying to get user view settings
				userId = token.getLogonName();
				viewDtos = viewSettingDao.getViewObjects(userId, null);
				System.err.println("NotificationEventServiceImpl.getViewList() user view objetcs" + viewDtos);

				// if not found then fetch admin view settings
				if (ServicesUtil.isEmpty(viewDtos)) {

					viewDtos = viewSettingDao.getViewObjects(PMCConstant.ADMIN, "1");
					System.err.println("NotificationEventServiceImpl.getViewList() admin objects" + viewDtos);

					if (viewDtos != null && viewDtos.size() > 0) {
						// if admin view settings are present the render it to
						// UI and save the same for User

						List<NotificationViewSettingsDto> userViewDtos = new ArrayList<>();
						for (NotificationViewSettingsDto notificationViewSettingsDto : viewDtos) {
							String newSettingId = UUID.randomUUID().toString();
							String settingId = notificationViewSettingsDto.getSettings();
							notificationViewSettingsDto.setUserId(userId);
							notificationViewSettingsDto.setSettings(newSettingId);
							notificationViewSettingsDto.setId("1");
							viewSettingDao.create(notificationViewSettingsDto);
							userViewDtos.add(notificationViewSettingsDto);
							notificationSettingDao.copyAdminSettings(settingId, newSettingId);
						}

						viewDtos = userViewDtos;
						System.err.println(
								"NotificationEventServiceImpl.getViewList() insert admin obj to user" + viewDtos);

					}
				}
			} else if (userId.equalsIgnoreCase(PMCConstant.ADMIN)) {
				userId = PMCConstant.ADMIN;
				viewDtos = viewSettingDao.getViewObjects(userId, isDefault);

			}

			responseDto.setViewDtos(viewDtos);

			if (viewDtos != null && viewDtos.size() > 0)
				message.setMessage(PMCConstant.READ_SUCCESS);
			else
				message.setMessage("No records found");

		} catch (Exception e) {
			message.setMessage(PMCConstant.READ_FAILURE);
			message.setStatus(PMCConstant.STATUS_FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
			System.err.println("NotificationEventServiceImpl.getViewList() error " + e.getMessage());
		}
		responseDto.setMessage(message);
		return responseDto;
	}

	@Override
	public NotificationViewDetailResponseDto getViewDetail(NotificationViewDetailRequestDto requestDto,Token token) {
		NotificationViewDetailResponseDto detailResponseDto = null;
		System.err.println("NotificationEventServiceImpl.getViewDetail() requestDto" + requestDto);
		try {

			// fetch user viewdetails from user_config_events
			if (!ServicesUtil.isEmpty(requestDto.getUserId())) {
				System.err.println("NotificationEventServiceImpl.getViewDetail() check If" + requestDto);
				detailResponseDto = notificationDao.getViewDetail(requestDto.getViewName(), requestDto.getViewType());
				System.err.println("NotificationEventServiceImpl.getViewDetail() detailResponseDto in if " + detailResponseDto);
			} else {
				System.err.println("NotificationEventServiceImpl.getViewDetail() check else" + requestDto);
				//String userId = UserManagementUtil.getLoggedInUser().getName().toUpperCase();
				//String userId = token.getLogonName().toUpperCase();
				String userId = token.getLogonName();
				System.err.println("NotificationEventServiceImpl.getViewDetail() userID" + userId);
				requestDto.setUserId(userId);
				detailResponseDto = notificationDao.getUserViewDetail(requestDto);
				System.err.println("NotificationEventServiceImpl.getViewDetail() detailResponseDto " + detailResponseDto);

				if (ServicesUtil.isEmpty(detailResponseDto) && !requestDto.getViewType().equals("Where")) {
					// get admin view detail
					System.err.println("NotificationEventServiceImpl.getViewDetail() detailResponseDto1 " + detailResponseDto);
					detailResponseDto = notificationDao.getViewDetail(null, null);
					System.err.println("NotificationEventServiceImpl.getViewDetail() detailResponseDto2 " + detailResponseDto);
					List<NotificationEventDto> eventDtoResult = new ArrayList<>();
					if (!ServicesUtil.isEmpty(detailResponseDto)
							&& !ServicesUtil.isEmpty(detailResponseDto.getEventDtos())
							&& !"Profile".equals(requestDto.getViewType())) {
						for (NotificationEventDto eventDto : detailResponseDto.getEventDtos()) {
							eventDto.setUserId(userId);
							eventDto.setType("General");

							if (ServicesUtil.isEmpty(eventDto.getChannelList())) {
								eventDto.setChannel("none");
								userConfigDao.saveOrUpdate(eventDto);

							}
							for (String channel : eventDto.getChannelList()) {

								eventDto.setChannel(channel);

								userConfigDao.saveOrUpdate(eventDto);
							}
							if (!ServicesUtil.isEmpty(requestDto.getViewName())
									&& requestDto.getViewName().equalsIgnoreCase(eventDto.getEventName())) {
								eventDtoResult.add(eventDto);
							}

						}

						if (eventDtoResult.size() > 0) {
							detailResponseDto.setEventDtos(eventDtoResult);
						}
						if (!"Profile".equals(requestDto.getViewType()))
							detailResponseDto = notificationDao.getUserViewDetail(requestDto);
					}
				}
				
				else if (ServicesUtil.isEmpty(detailResponseDto) && requestDto.getViewType().equals("Where")) {
					// get admin view detail
					detailResponseDto = notificationDao.getViewDetail(null, null);
					System.err.println("NotificationEventServiceImpl.getViewDetail() Response  " + detailResponseDto);
				}
				
			}
			System.err.println("NotificationEventServiceImpl.getViewDetail() Response1  " + detailResponseDto);

			return detailResponseDto;
		}

		catch (Exception e) {
			System.err.println("NotificationEventServiceImpl.getViewDetail() error " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public NotificationProfileResponseDto getProfileList(String isAdmin,Token token) {
		NotificationProfileResponseDto responseDto = new NotificationProfileResponseDto();
		ResponseMessage message = new ResponseMessage();
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			String user=token.getLogonName();
			System.err.println("NotificationEventServiceImpl.getViewList() user " + user);
			//String user="Admin";
			List<NotificationProfileSettingDto> profileDtos = notificationProfileSettingDao
					.getResult(user, isAdmin);
			System.err.println("NotificationEventServiceImpl.getViewList() profileDto " +profileDtos);
			if (profileDtos != null && profileDtos.size() > 0) {
				message.setMessage(PMCConstant.READ_SUCCESS);
				message.setStatus(PMCConstant.SUCCESS);
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
				responseDto.setProfileDto(profileDtos);

			} else {
				message.setMessage("No records found");
				message.setStatus(PMCConstant.STATUS_SUCCESS);
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
				responseDto.setProfileDto(profileDtos);
			}
		} catch (Exception e) {
			message.setMessage(PMCConstant.READ_FAILURE);
			message.setStatus(PMCConstant.STATUS_FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
			System.err.println("NotificationEventServiceImpl.getViewList() error " + e.getMessage());
		}
		responseDto.setResponseMessage(message);
		System.err.println("NotificationEventServiceImpl.getViewList() responseDto " +responseDto);
		return responseDto;

	}

	@Override
	public ResponseMessage createProfile(NotificationProfileSettingDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			String user=token.getLogonName();
			dto.setUserId(user);
			dto.setProfileId(UUID.randomUUID().toString());
			dto.setSettingId(UUID.randomUUID().toString());
			notificationProfileSettingDao.updateOldProfile(user);
			createNewViewData(dto.getProfileId(), "Profile");
			notificationProfileSettingDao.create(dto);
			createSetting(dto.getSettingId());
			responseDto.setMessage("Profile " + PMCConstant.CREATED_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Failed to create profile");
			System.err.println("[WBP-Dev][Workbox][PMC][Profile][create][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage updateProfile(NotificationProfileSettingDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			String user=token.getLogonName();
			if (dto.getUserId().equals("Admin")
					&& (dto.getProfileName().equals("Silent Mode") || dto.getProfileName().equals("Airplane Mode")
							|| dto.getProfileName().equals("Do Not Disturb Mode"))) {
				dto.setUserId(user);
				dto.setProfileId(UUID.randomUUID().toString());
				dto.setSettingId(UUID.randomUUID().toString());
				notificationProfileSettingDao.updateOldProfile(user);
				createNewViewData(dto.getProfileId(), "Profile");
				notificationProfileSettingDao.create(dto);
				createSetting(dto.getSettingId());
			} else {
				notificationProfileSettingDao.updateOldProfile(user);
				notificationProfileSettingDao.update(dto);
			}
			responseDto.setMessage("Profile " + PMCConstant.UPDATE_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Failed to update profile");
			System.err.println("[WBP-Dev][Workbox][PMC][Profile][update][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage deleteProfile(NotificationProfileSettingDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			if (dto.getUserId().equals("Admin")
					&& (dto.getProfileName().equals("Silent Mode") || dto.getProfileName().equals("Airplane Mode")
							|| dto.getProfileName().equals("Do Not Disturb Mode"))) {

				responseDto.setMessage("Cannot delete default Profiles");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				notificationProfileSettingDao.deleteProfiles(dto);
				responseDto.setMessage("Profile " + PMCConstant.DELETE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to delete profile");
			System.err.println("[WBP-Dev][Workbox][PMC][Profile][delete][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public NotificationSettingResponseDto getSettingDetails(String settingId, String tabType, String isAdmin, String adminSetting,Token token) {
		ResponseMessage message = new ResponseMessage();
		NotificationSettingResponseDto responseDto = new NotificationSettingResponseDto();
		try {
			
			if (!ServicesUtil.isEmpty(adminSetting)) 
				settingId = notificationSettingDao.getAdminSettingId(settingId);
				
			List<NotificationSettingDto> viewDtos = notificationDao.getSettingDetail(settingId, tabType, isAdmin,token);

			if (viewDtos != null && viewDtos.size() > 0) {
				message.setMessage(PMCConstant.READ_SUCCESS);
				message.setStatus(PMCConstant.SUCCESS);
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
				responseDto.setSettingDtos(viewDtos);

			} else {
				message.setMessage("No records found");
				message.setStatus(PMCConstant.STATUS_SUCCESS);
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
				responseDto.setSettingDtos(viewDtos);
			}
		} catch (Exception e) {
			message.setMessage(PMCConstant.READ_FAILURE);
			message.setStatus(PMCConstant.STATUS_FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
			System.err.println("NotificationEventServiceImpl.getSettingDetails() error " + e.getMessage());
		}
		responseDto.setMessage(message);
		return responseDto;
	}

	@Override
	public ResponseMessage updateSettings(List<NotificationSettingDto> dtos) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			String status = notificationSettingDao.saveOrUpdate(dtos);
			if (PMCConstant.SUCCESS.equals(status)) {
				responseDto.setMessage("Settings " + PMCConstant.UPDATE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to update settings");
			System.err.println("[WBP-Dev][Workbox][PMC][Settings][update][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage deleteSettings(List<NotificationSettingDto> dtos) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			String status = notificationSettingDao.saveOrUpdate(dtos);
			if (PMCConstant.SUCCESS.equals(status)) {
				responseDto.setMessage("Settings " + PMCConstant.DELETE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to delete settings");
			System.err.println("[WBP-Dev][Workbox][PMC][Settings][update][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage updateViewDetail(NotificationViewDetailResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			List<NotificationEventDto> eventDtos = dto.getEventDtos();
			String eventIds = "";
			for (NotificationEventDto notificationEventDto : eventDtos) {
				eventIds += notificationEventDto.getEventId() + "','";
			}

			if (!"Profile".equals(dto.getViewType())) {
				userConfigDao.deleteUserConfig(eventIds,token);
				for (NotificationEventDto notificationEventDto : eventDtos) {
					if (!ServicesUtil.isEmpty(notificationEventDto.getChannelList())) {
						for (String channel : notificationEventDto.getChannelList()) {
							notificationEventDto.setChannel(channel);
							notificationEventDto.setUserId(token.getLogonName());
							userConfigDao.saveOrUpdate(notificationEventDto);
						}
					}
				}
			} else {
				String userId = userConfigDao.getUserProfile(dto.getViewName(),token);
				userConfigDao.deleteProfileConfig(userId);
				for (NotificationEventDto notificationEventDto : eventDtos) {
					if (!ServicesUtil.isEmpty(notificationEventDto.getChannelList())) {
						for (String channel : notificationEventDto.getChannelList()) {
							notificationEventDto.setChannel(channel);
							notificationEventDto.setType("Profile");
							notificationEventDto.setUserId(userId);
							notificationEventDto.setEventId(notificationEventDto.getEventId());
							userConfigDao.saveOrUpdate(notificationEventDto);
							// userConfigDao.saveOrUpdate(notificationEventDto);
						}
					}
				}
			}

			responseDto.setMessage("Channels " + PMCConstant.UPDATE_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Channel " + PMCConstant.UPDATE_FAILURE);
			System.err.println("NotificationEventServiceImpl.updateViewDetail() error" + e.getMessage());

		}
		return responseDto;

	}

	public void createNewViewData(String userId, String viewName) {
		NotificationViewDetailResponseDto detailResponseDto = null;

		if (ServicesUtil.isEmpty(detailResponseDto)) {
			// get admin view detail
			detailResponseDto = notificationDao.getViewDetail(null, null);
			List<NotificationEventDto> eventDtoResult = new ArrayList<>();
			if (!ServicesUtil.isEmpty(detailResponseDto) && !ServicesUtil.isEmpty(detailResponseDto.getEventDtos())) {
				for (NotificationEventDto eventDto : detailResponseDto.getEventDtos()) {
					eventDto.setUserId(userId);
					eventDto.setType("Profile");

					//if (ServicesUtil.isEmpty(eventDto.getChannelList())) {
						eventDto.setChannel("None");
						userConfigDao.saveOrUpdate(eventDto);

					/*for (String channel : eventDto.getChannelList()) {

						eventDto.setChannel(channel);

						userConfigDao.saveOrUpdate(eventDto);
					}*/
					if (!ServicesUtil.isEmpty(viewName) && viewName.equalsIgnoreCase(eventDto.getEventName())) {
						eventDtoResult.add(eventDto);
					}

				}

				if (eventDtoResult.size() > 0) {
					detailResponseDto.setEventDtos(eventDtoResult);
				}
			}
		}

	}

	public ResponseMessage createSetting(String settingId) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			List<NotificationSettingDto> dtoList = new ArrayList<>();
			dtoList = notificationSettingDao.getResult(settingId);
			for (NotificationSettingDto notificationSettingDto : dtoList) {
				notificationSettingDto.setProfileSettingId(settingId);
				notificationSettingDao.create(notificationSettingDto);
			}
			responseDto.setMessage("Profile " + PMCConstant.CREATED_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Failed to create setting");
			System.err.println("[WBP-Dev][Workbox][PMC][setting][create][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public NotificationAllSettingResponseDto getAllSettingDetails(String userId,Token token) {
		ResponseMessage message = new ResponseMessage();
		NotificationAllSettingResponseDto objectDto = new NotificationAllSettingResponseDto();
		NotificationSettingResponseDto responseDto = new NotificationSettingResponseDto();
		try {

			if (ServicesUtil.isEmpty(userId))
				//userId = UserManagementUtil.getLoggedInUser().getName();
				userId=token.getLogonName();

			List<String> events = notificationDao.getAllEvents();

			for (String event : events) {

				message = new ResponseMessage();
				responseDto = new NotificationSettingResponseDto();

				List<NotificationSettingDto> settingDtos = notificationDao.getAllSettingDetail(userId, event);

				if (settingDtos != null && settingDtos.size() > 0) {
					message.setMessage(PMCConstant.READ_SUCCESS);
					message.setStatus(PMCConstant.SUCCESS);
					message.setStatusCode(PMCConstant.CODE_SUCCESS);
					responseDto.setSettingDtos(settingDtos);

				} else {
					message.setMessage("No records found");
					message.setStatus(PMCConstant.STATUS_SUCCESS);
					message.setStatusCode(PMCConstant.CODE_SUCCESS);
					responseDto.setSettingDtos(settingDtos);
				}

				responseDto.setMessage(message);

				if (NotificationConstant.TASK.equals(event))
					objectDto.setTaskSettingDtos(responseDto);

				if (NotificationConstant.CHAT.equals(event))
					objectDto.setChatSettingDtos(responseDto);

				if (NotificationConstant.PEOPLE.equals(event))
					objectDto.setPeopleSettingDtos(responseDto);

				if (NotificationConstant.APPLICATION.equals(event))
					objectDto.setApplicationSettingDtos(responseDto);

				if (NotificationConstant.REPORTS.equals(event))
					objectDto.setReportsSettingDtos(responseDto);

				if (objectDto != null) {
					message = new ResponseMessage();
					message.setMessage(PMCConstant.READ_SUCCESS);
					message.setStatus(PMCConstant.SUCCESS);
					message.setStatusCode(PMCConstant.CODE_SUCCESS);

				} else {
					message = new ResponseMessage();
					message.setMessage("No records found");
					message.setStatus(PMCConstant.STATUS_SUCCESS);
					message.setStatusCode(PMCConstant.CODE_SUCCESS);
				}

				objectDto.setMessage(message);

			}

		} catch (Exception e) {
			message.setMessage(PMCConstant.READ_FAILURE);
			message.setStatus(PMCConstant.STATUS_FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
			System.err.println("NotificationEventServiceImpl.getSettingDetails() error " + e.getMessage());
		}

		return objectDto;
	}

	@Override
	public ResponseMessage updateApplicationUpdate(ApplicationUpdatesDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		try {
			dto.setId(UUID.randomUUID().toString());
			dto.setDateOfRelease(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));

			System.err.println("NotificationEventServiceImpl.updateApplicationUpdate()  " + dto);
			applicationUpdatesDao.create(dto);
			responseDto.setMessage("Application " + PMCConstant.UPDATE_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Failed to update Application");
			System.err.println("[WBP-Dev][Workbox][PMC][Application][update][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage updateViewList(NotificationViewResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		try {

			System.err.println("NotificationEventServiceImpl.updateViewList()");

			// role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				//String adminUser = UserManagementUtil.getLoggedInUser().getName();
				String adminUser=token.getLogonName();
				for (NotificationViewSettingsDto notificationViewSettingsDto : dto.getViewDtos()) {
					notificationViewSettingsDto.setUserId(role);
					notificationViewSettingsDto.setIsDefault(notificationViewSettingsDto.getIsEnable());
					notificationViewSettingsDto.setId("2");
					notificationViewSettingsDto.setAdminUser(adminUser);
					viewSettingDao.saveOrUpdate(notificationViewSettingsDto);

					if (notificationViewSettingsDto.getViewType().equals("Where")
							&& notificationViewSettingsDto.getIsEnable().equals(false)) {
						crossConstantDao.deleteCrossConstant(notificationViewSettingsDto.getViewName(),
								"Notification Channels");
					} else if (notificationViewSettingsDto.getViewType().equals("Where")
							&& notificationViewSettingsDto.getIsEnable().equals(true)) {
						CrossConstantDto crossConstantDto = new CrossConstantDto();
						crossConstantDto.setConstantId("Notification Channels");
						crossConstantDto.setConstantName(notificationViewSettingsDto.getViewName());
						crossConstantDto.setConstantValue(notificationViewSettingsDto.getViewName());
						crossConstantDao.saveOrUpdateCrossConstant(crossConstantDto);
					}
				}

				responseDto.setMessage("View List " + PMCConstant.UPDATE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			} else {
				responseDto
						.setMessage("View List " + PMCConstant.UPDATE_FAILURE + ". Admin only have access to update");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {

			responseDto.setMessage("Failed to update view List");
			System.err.println("NotificationEventServiceImpl.updateViewList() error " + e.getMessage());

		}
		return responseDto;

	}

	@Override
	public ResponseMessage updateViewDetailByAdmin(NotificationViewDetailResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		try {

			System.err.println("NotificationEventServiceImpl.updateViewDetailByAdmin()");

			// role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				//String adminUser = UserManagementUtil.getLoggedInUser().getName();
				String adminUser=token.getLogonName();
				System.err.println("NotificationEventServiceImpl.updateViewDetailByAdmin() checking");
				for (NotificationEventDto notificationEventDto : dto.getEventDtos()) {
					notificationEventDto.setEventId(notificationEventDto.getEventId());
					notificationEventDto.setIsDefault(notificationEventDto.getIsEnable());
					notificationEventDto.setId("2");
					notificationEventDto.setAdminUser(adminUser);
					notificationDao.saveOrUpdate(notificationEventDto);
				}

				responseDto.setMessage("View Detail " + PMCConstant.UPDATE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			} else {
				responseDto
						.setMessage("View Detail " + PMCConstant.UPDATE_FAILURE + ". Admin only have access to update");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {

			responseDto.setMessage("Failed to update view detail");
			System.err.println("NotificationEventServiceImpl.updateViewDetailByAdmin() error " + e.getMessage());

		}
		return responseDto;

	}

	@Override
	public ResponseMessage createWhatObject(NotificationViewResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {

			System.err.println("NotificationEventServiceImpl.createWhatObject()");

			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			System.err.println("NotificationEventServiceImpl.create channel role to Admin");
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				//String adminUser = UserManagementUtil.getLoggedInUser().getName();
				String adminUser=token.getLogonName();
				System.err.println("NotificationEventServiceImpl.createWhatObject() logon user"+adminUser);
				for (NotificationViewSettingsDto notificationViewSettingsDto : dto.getViewDtos()) {
					String newSettingId = UUID.randomUUID().toString();
					notificationViewSettingsDto.setUserId(role);
					notificationViewSettingsDto.setIsDefault(true);
					notificationViewSettingsDto.setId("2");
					notificationViewSettingsDto.setAdminUser(adminUser);
					notificationViewSettingsDto.setSettings(newSettingId);
					viewSettingDao.create(notificationViewSettingsDto);
					notificationSettingDao.copyAdminSettings("1", newSettingId);

					notificationViewSettingsDto.setId("1");
					viewSettingDao.create(notificationViewSettingsDto);

					// Getting already configured user
					List<String> existingUsers = viewSettingDao.getExistingUsers();

					notificationViewSettingsDto.setAdminUser(null);
					// Adding newly created object to existing user
					for (String existingUser : existingUsers) {

						newSettingId = UUID.randomUUID().toString();

						notificationViewSettingsDto.setSettings(newSettingId);
						notificationViewSettingsDto.setUserId(existingUser);

						viewSettingDao.create(notificationViewSettingsDto);
						// Copying Admin settings to Newly created object
						notificationSettingDao.copyAdminSettings("1", newSettingId);

					}
				}

				responseDto.setMessage(dto.getViewDtos().get(0).getViewName() + " Object " + PMCConstant.CREATED_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			} else {
				responseDto
						.setMessage("What Object " + PMCConstant.CREATE_FAILURE + ". Admin only have access to update");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {
			responseDto.setMessage("Failed to create object");
			System.err.println("[WBP-Dev][Workbox][PMC][object][create][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage createEvents(NotificationViewDetailResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		try {

			System.err.println("NotificationEventServiceImpl.createEvents()");

			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			System.err.println("NotificationEventServiceImpl.create channel role to Admin");
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				//String adminUser = UserManagementUtil.getLoggedInUser().getName();
				String adminUser=token.getLogonName();
				for (NotificationEventDto notificationEventDto : dto.getEventDtos()) {
					String[] eventName = notificationEventDto.getEventName().split(" ");
					StringBuilder eventid = new StringBuilder();

					for (int i = 0; i < eventName.length; i++) {
						eventid.append(eventName[i].charAt(0));
						eventid.append(eventName[i].charAt(1));
						eventid.append(eventName[i].charAt(eventName[i].length() - 1));
					}

					String eventId = new String(eventid).toUpperCase();
					notificationEventDto.setEventId(eventId);
					notificationEventDto.setIsDefault(true);
					notificationEventDto.setId("2");
					notificationEventDto.setAdminUser(adminUser);
					notificationDao.create(notificationEventDto);

					System.err.println("NotificationEventServiceImpl.createEvents() by Default");
					notificationEventDto.setId("1");
					notificationDao.create(notificationEventDto);

					// Getting all channels
					// Adding new events with all channels
					List<String> channels = notificationEventChannelDao.getAllChannels();
					System.err.println("NotificationEventServiceImpl.createEvents() channels " + channels);
					NotificationEventChannelDto notificationEventChannelDto = new NotificationEventChannelDto();
					for (String channel : channels) {
						notificationEventChannelDto.setChannel(channel);
						notificationEventChannelDto.setEventId(notificationEventDto.getEventId());
						notificationEventChannelDto.setIsDefault(true);

						notificationEventChannelDao.create(notificationEventChannelDto);

					}

					// Getting already configured user
					List<String> existingUsers = userConfigDao.getAllUserConfigured();

					// Adding newly created events to existing user in user
					// config table
					for (String existingUser : existingUsers) {

						notificationEventDto.setUserId(existingUser);
						notificationEventDto.setType("General");
						for (String channel : channels) {
							notificationEventDto.setChannel(channel);
							userConfigDao.saveOrUpdate(notificationEventDto);
						}

					}
				}
				responseDto.setMessage("Events " + PMCConstant.CREATED_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			} else {
				responseDto.setMessage("Events " + PMCConstant.CREATE_FAILURE + ". Admin only have access to update");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {

			responseDto.setMessage("Failed to create Events. Event Name Already Exists");
			System.err.println("NotificationEventServiceImpl.createEvents() error " + e.getMessage());
			e.printStackTrace();

		}
		return responseDto;

	}

	@Override
	public ResponseMessage createChannel(NotificationViewResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {

			System.err.println("NotificationEventServiceImpl.createChannel()");

			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			System.err.println("NotificationEventServiceImpl.create channel role to Admin");
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {

				for (NotificationViewSettingsDto notificationViewSettingsDto : dto.getViewDtos()) {

					List<String> eventids = notificationEventChannelDao.getAllEvents();
					System.err.println("NotificationEventServiceImpl.createChannel() eventsids " + eventids);
					NotificationEventChannelDto notificationEventChannelDto = new NotificationEventChannelDto();
					for (String eventid : eventids) {
						notificationEventChannelDto.setChannel(notificationViewSettingsDto.getViewName());
						notificationEventChannelDto.setEventId(eventid);
						notificationEventChannelDto.setIsDefault(true);

						notificationEventChannelDao.create(notificationEventChannelDto);

					}
					//String adminUser = UserManagementUtil.getLoggedInUser().getName();
					String adminUser=token.getLogonName();
					System.err.println("NotificationEventServiceImpl.createChannel() logedin user" +adminUser);
					String newSettingId = UUID.randomUUID().toString();
					notificationViewSettingsDto.setUserId(role);
					notificationViewSettingsDto.setIsDefault(true);
					notificationViewSettingsDto.setId("2");
					notificationViewSettingsDto.setAdminUser(adminUser);
					notificationViewSettingsDto.setViewName(notificationViewSettingsDto.getViewName());
					notificationViewSettingsDto.setViewType("Where");
					notificationViewSettingsDto.setSettings(newSettingId);
					System.err.println("setting the notification view settings");
					viewSettingDao.create(notificationViewSettingsDto);
					notificationSettingDao.copyAdminSettings("1", newSettingId);

					notificationViewSettingsDto.setId("1");
					viewSettingDao.create(notificationViewSettingsDto);

					// Getting already configured user
					List<String> existingUsers = viewSettingDao.getExistingUsers();

					notificationViewSettingsDto.setAdminUser(null);
					// Adding newly created object to existing user
					for (String existingUser : existingUsers) {

						newSettingId = UUID.randomUUID().toString();

						notificationViewSettingsDto.setSettings(newSettingId);
						notificationViewSettingsDto.setUserId(existingUser);

						viewSettingDao.create(notificationViewSettingsDto);
						// Copying Admin settings to Newly created object
						notificationSettingDao.copyAdminSettings("1", newSettingId);

					}

					CrossConstantDto crossConstantDto = new CrossConstantDto();
					crossConstantDto.setConstantId("Notification Channels");
					crossConstantDto.setConstantName(notificationViewSettingsDto.getViewName());
					crossConstantDto.setConstantValue(notificationViewSettingsDto.getViewName());
					crossConstantDao.saveOrUpdateCrossConstant(crossConstantDto);

					responseDto.setMessage(dto.getViewDtos().get(0).getViewName() + " Channel " + PMCConstant.CREATED_SUCCESS);
					responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
					responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
				}

			} else {
				responseDto.setMessage("Channel " + PMCConstant.CREATE_FAILURE + ". Admin only have access to update");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {
			responseDto.setMessage("Failed to create Channel");
			System.err.println("[WBP-Dev][Workbox][PMC][createChannel][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage createProfileByAdmin(NotificationProfileSettingDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {

			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {

				dto.setUserId(role);
				dto.setProfileId(UUID.randomUUID().toString());
				dto.setSettingId(UUID.randomUUID().toString());
				dto.setIsActive(true);
				createNewViewData(dto.getProfileId(), "Profile");
				notificationProfileSettingDao.create(dto);
				createSetting(dto.getSettingId());
				responseDto.setMessage("Profile " + PMCConstant.CREATED_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				responseDto.setMessage("Cannot create default Profiles. Admin only have access to create.");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to create profile");
			System.err.println("[WBP-Dev][Workbox][PMC][Profile][create][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage deleteProfileByAdmin(NotificationProfileSettingDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {

				String result=notificationProfileSettingDao.deleteProfilesByAdmin(dto);
				if(result=="SUCCESS") {
				responseDto.setMessage("Profile " + PMCConstant.DELETE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
				}
			} else {
				responseDto.setMessage("Cannot delete default Profiles. Admin only have access to delete.");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to delete profile");
			System.err.println("[WBP-Dev][Workbox][PMC][Profile][delete][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage updateProfileByAdmin(NotificationProfileSettingDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {

				notificationProfileSettingDao.updateAdminProfile(role, dto.getProfileName(), dto.getIsEnable());

			} else {
				responseDto.setMessage("Cannot update default Profiles. Admin only have access to update.");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
			responseDto.setMessage("Profile " + PMCConstant.UPDATE_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Failed to update profile");
			System.err.println("[WBP-Dev][Workbox][PMC][Profile][update][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage updateSettingsByAdmin(List<NotificationSettingDto> dtos,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {

			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole(token);
			String role="Admin";
			String settingId = "";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				settingId = notificationSettingDao.getAdminSettingId(dtos.get(0).getProfileSettingId());
				if (ServicesUtil.isEmpty(settingId))
						settingId=dtos.get(0).getProfileSettingId();
				for (NotificationSettingDto settingDto : dtos) {
					//status = notificationSettingDao.updateSettingsByAdmin(settingDto);
					settingDto.setProfileSettingId(settingId);
					settingDto.setIsDefault(settingDto.getIsEnable());
				}
				System.err.println("[WBP-Dev][Workbox][PMC][Settings][update][dtos]  " + dtos);
				String status = notificationSettingDao.saveOrUpdate(dtos);
				if (PMCConstant.SUCCESS.equals(status)) {
					responseDto.setMessage("Admin Settings " + PMCConstant.UPDATE_SUCCESS);
					responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
					responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
				}
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to update admin settings");
			System.err.println("[WBP-Dev][Workbox][PMC][Settings][update][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}
	
	@Override
	public ResponseMessage deleteViewList(NotificationViewSettingsDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole();
			String role = "Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				viewSettingDao.deleteViewList(dto.getViewName());
				notificationDao.deleteEventGroup(dto.getViewName());
				notificationEventChannelDao.deleteEventGroup(dto.getViewName());
				userConfigDao.deleteEventGroup(dto.getViewName());

				responseDto.setMessage("View List " + PMCConstant.DELETE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				responseDto.setMessage(PMCConstant.DELETE_FAILURE + ". Admin only have access to delete");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to delete view list");
			System.err.println("[WBP-Dev][Workbox][PMC][viewList][delete][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseMessage deleteViewDetailByAdmin(NotificationViewDetailResponseDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		try {

			System.err.println("NotificationEventServiceImpl.deleteViewDetailByAdmin()");

			// role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole();
			String role = "Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				for (NotificationEventDto notificationEventDto : dto.getEventDtos()) {
					notificationEventDto.setEventId(notificationEventDto.getEventId());

					notificationDao.deleteEvents(notificationEventDto.getEventName());
					userConfigDao.deleteViewDetail(notificationEventDto.getEventId());
					notificationEventChannelDao.deleteEventId(notificationEventDto.getEventId());
				}

				responseDto.setMessage("View Detail " + PMCConstant.UPDATE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			} else {
				responseDto.setMessage(PMCConstant.DELETE_FAILURE + ". Admin only have access to delete");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to delete view Detail");
			System.err.println("[WBP-Dev][Workbox][PMC][viewDetail][delete][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;

	}


	@Override
	public ResponseMessage deleteChannel(NotificationViewSettingsDto dto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			// Role - to get whether user is admin or not
			//String role = viewSettingDao.getUserRole();
			String role = "Admin";
			if (!ServicesUtil.isEmpty(role) && PMCConstant.ADMIN.equalsIgnoreCase(role)) {
				viewSettingDao.deleteViewList(dto.getViewName());
				userConfigDao.deleteChannel(dto.getViewName());
				notificationEventChannelDao.deleteChannel(dto.getViewName());
				crossConstantDao.deleteCrossConstant(dto.getViewName(), "Notification Channels");
				responseDto.setMessage("Channel " + PMCConstant.DELETE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				responseDto.setMessage(PMCConstant.DELETE_FAILURE + ". Admin only have access to delete");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to deleteChannel");
			System.err.println("[WBP-Dev][Workbox][PMC][deleteChannel][delete][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}
}
