package oneapp.incture.workbox.demo.notification.service;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.notification.dto.ApplicationUpdatesDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationAllSettingResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationProfileResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationProfileSettingDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationSettingDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationSettingResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewDetailRequestDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewDetailResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewResponseDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewSettingsDto;

public interface NotificationEventService {

	// View Settings
	NotificationViewResponseDto getViewList(String userId, String isDefault, Token token);

	NotificationViewDetailResponseDto getViewDetail(NotificationViewDetailRequestDto requestDto,Token token);

	ResponseMessage updateViewDetail(NotificationViewDetailResponseDto dto,Token token);

	// Settings services

	//NotificationSettingResponseDto getSettingDetails(String settingId);

	ResponseMessage updateSettings(List<NotificationSettingDto> dto);

	ResponseMessage deleteSettings(List<NotificationSettingDto> dto);

	// Profile Services

	NotificationProfileResponseDto getProfileList(String isAdmin,Token token);

	ResponseMessage createProfile(NotificationProfileSettingDto dto,Token token);

	ResponseMessage updateProfile(NotificationProfileSettingDto dto,Token token);

	ResponseMessage deleteProfile(NotificationProfileSettingDto dto);

	NotificationSettingResponseDto getSettingDetails(String settingId, String tabType, String isAdmin, String adminSetting,Token token);

	NotificationAllSettingResponseDto getAllSettingDetails(String userId,Token token);

	ResponseMessage updateApplicationUpdate(ApplicationUpdatesDto dto);

	ResponseMessage updateViewList(NotificationViewResponseDto dto,Token token);

	ResponseMessage updateViewDetailByAdmin(NotificationViewDetailResponseDto dto,Token token);

	ResponseMessage createWhatObject(NotificationViewResponseDto dto,Token token);

	ResponseMessage createEvents(NotificationViewDetailResponseDto dto,Token token);

	ResponseMessage createChannel(NotificationViewResponseDto dto,Token token);

	ResponseMessage createProfileByAdmin(NotificationProfileSettingDto dto,Token token);

	ResponseMessage deleteProfileByAdmin(NotificationProfileSettingDto dto,Token token);

	ResponseMessage updateProfileByAdmin(NotificationProfileSettingDto dto,Token token);

	ResponseMessage updateSettingsByAdmin(List<NotificationSettingDto> dtos,Token token);
	
	ResponseMessage deleteViewList(NotificationViewSettingsDto dto,Token token);

	ResponseMessage deleteViewDetailByAdmin(NotificationViewDetailResponseDto dto,Token token);

	ResponseMessage deleteChannel(NotificationViewSettingsDto dto,Token token);

}
