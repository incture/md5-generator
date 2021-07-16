package oneapp.incture.workbox.demo.notification.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
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
import oneapp.incture.workbox.demo.notification.service.NotificationEventService;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/notificationConfiguration", produces = "application/json")
public class NotificationEventController {

	@Autowired
	NotificationEventService notificationService;

	// CRUD for notification object

	@RequestMapping(value = "/getViewList", method = RequestMethod.GET)
	public NotificationViewResponseDto getViewList(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "isDefault", required = false) String isDefault,@AuthenticationPrincipal Token token) {

		return notificationService.getViewList(userId, isDefault,token);
	}

	@RequestMapping(value = "/getViewDetail", method = RequestMethod.POST)
	public NotificationViewDetailResponseDto getViewDetail(@RequestBody NotificationViewDetailRequestDto requestDto
			,@AuthenticationPrincipal Token token) {
		return notificationService.getViewDetail(requestDto,token);
	}

	@RequestMapping(value = "/updateViewDetail", method = RequestMethod.POST)
	public ResponseMessage updateViewDetail(@RequestBody NotificationViewDetailResponseDto dto
			,@AuthenticationPrincipal Token token) {

		return notificationService.updateViewDetail(dto,token);
	}

	// CRUD for user notification settings

	@RequestMapping(value = "/getSettingDetails", method = RequestMethod.GET)
	public NotificationSettingResponseDto getSettingDetails(
			@RequestParam(value = "settingId", required = false) String settingId,
			@RequestParam(value = "tabType", required = false) String tabType,
			@RequestParam(value = "isAdmin", required = false) String isAdmin,
			@RequestParam(value = "adminSetting", required = false) String adminSetting,
			@AuthenticationPrincipal Token token) {

		return notificationService.getSettingDetails(settingId, tabType, isAdmin,adminSetting,token);
	}

	@RequestMapping(value = "/getAllSettingDetails", method = RequestMethod.GET)
	public NotificationAllSettingResponseDto getAllSettingDetails(
			@RequestParam(value = "userId", required = false) String userId
			,@AuthenticationPrincipal Token token) {

		return notificationService.getAllSettingDetails(userId,token);
	}

	@RequestMapping(value = "/updateSettings", method = RequestMethod.POST)
	public ResponseMessage updateSettings(@RequestBody List<NotificationSettingDto> dtos) {

		return notificationService.updateSettings(dtos);
	}

	// @RequestMapping(value = "/deleteSettings", method = RequestMethod.POST)
	// public ResponseMessage delete(@RequestBody List<NotificationSettingDto>
	// dtos) {
	//
	// return notificationService.deleteSettings(dtos);
	// }

	// CRUD for Profile

	@RequestMapping(value = "/createProfile", method = RequestMethod.POST)
	public ResponseMessage create(@RequestBody NotificationProfileSettingDto dto,@AuthenticationPrincipal Token token) {

		return notificationService.createProfile(dto,token);
	}

	@RequestMapping(value = "/getProfiles", method = RequestMethod.GET)
	public NotificationProfileResponseDto getProfileList(
			@RequestParam(value = "isAdmin", required = false) String isAdmin
			,@AuthenticationPrincipal Token token) {

		return notificationService.getProfileList(isAdmin,token);
	}

	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public ResponseMessage update(@RequestBody NotificationProfileSettingDto dto
			,@AuthenticationPrincipal Token token) {

		return notificationService.updateProfile(dto,token);
	}

	@RequestMapping(value = "/deleteProfile", method = RequestMethod.POST)
	public ResponseMessage delete(@RequestBody NotificationProfileSettingDto dto) {

		return notificationService.deleteProfile(dto);
	}

	@RequestMapping(value = "/updateApplicationUpdate", method = RequestMethod.POST)
	public ResponseMessage updateApplicationUpdate(@RequestBody ApplicationUpdatesDto dto) {

		return notificationService.updateApplicationUpdate(dto);
	}

	@RequestMapping(value = "/updateViewList", method = RequestMethod.POST)
	public ResponseMessage updateViewList(@RequestBody NotificationViewResponseDto dto
			,@AuthenticationPrincipal Token token) {

		try {
			return notificationService.updateViewList(dto,token);
		} catch (Exception e) {
			ResponseMessage responseDto = new ResponseMessage();
			responseDto.setStatus(PMCConstant.STATUS_FAILURE);
			responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
			responseDto.setMessage("Failed to Update View List.");
			return responseDto;
		}
	}

	@RequestMapping(value = "/updateViewDetailByAdmin", method = RequestMethod.POST)
	public ResponseMessage updateViewDetailByAdmin(@RequestBody NotificationViewDetailResponseDto dto
			,@AuthenticationPrincipal Token token) {

		try {
			return notificationService.updateViewDetailByAdmin(dto,token);
		} catch (Exception e) {
			ResponseMessage responseDto = new ResponseMessage();
			responseDto.setStatus(PMCConstant.STATUS_FAILURE);
			responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
			responseDto.setMessage("Failed to Update View Detail.");
			return responseDto;
		}
	}

	@RequestMapping(value = "/createWhatObject", method = RequestMethod.POST)
	public ResponseMessage createWhatObject(@RequestBody NotificationViewResponseDto dto
			,@AuthenticationPrincipal Token token) {
		System.err.println("NotificationEventServiceImpl.create WhatObject conterller");
			return notificationService.createWhatObject(dto, token);
	

	}

	@RequestMapping(value = "/createEvents", method = RequestMethod.POST)
	public ResponseMessage createEventGroup(@RequestBody NotificationViewDetailResponseDto dto
			,@AuthenticationPrincipal Token token) {
		System.err.println("NotificationEventServiceImpl.create events conterller");
			return notificationService.createEvents(dto, token);
	
	}

	@RequestMapping(value = "/createChannel", method = RequestMethod.POST)
	public ResponseMessage createChannel(@RequestBody NotificationViewResponseDto dto
			,@AuthenticationPrincipal Token token) {
		System.err.println("NotificationEventServiceImpl.create channel conterller");
			return notificationService.createChannel(dto,token);
	
	}
	
	@RequestMapping(value = "/createProfileByAdmin", method = RequestMethod.POST)
	public ResponseMessage createProfileByAdmin(@RequestBody NotificationProfileSettingDto dto,
			@AuthenticationPrincipal Token token) {

		return notificationService.createProfileByAdmin(dto,token);
	}
	
	@RequestMapping(value = "/deleteProfileByAdmin", method = RequestMethod.POST)
	public ResponseMessage deleteProfileByAdmin(@RequestBody NotificationProfileSettingDto dto,@AuthenticationPrincipal Token token) {

		return notificationService.deleteProfileByAdmin(dto,token);
	}
	

	@RequestMapping(value = "/updateProfileByAdmin", method = RequestMethod.POST)
	public ResponseMessage updateProfileByAdmin(@RequestBody NotificationProfileSettingDto dto,@AuthenticationPrincipal Token token) {

		return notificationService.updateProfileByAdmin(dto,token);
	}
	
	@RequestMapping(value = "/updateSettingsByAdmin", method = RequestMethod.POST)
	public ResponseMessage updateSettingsByAdmin(@RequestBody List<NotificationSettingDto> dtos,@AuthenticationPrincipal Token token) {

		return notificationService.updateSettingsByAdmin(dtos,token);
	}
	
	@RequestMapping(value = "/deleteViewList", method = RequestMethod.POST)
	public ResponseMessage deleteViewList(@RequestBody NotificationViewSettingsDto dto,@AuthenticationPrincipal Token token) {

		return notificationService.deleteViewList(dto,token);
	}
	
	@RequestMapping(value = "/deleteViewDetailByAdmin", method = RequestMethod.POST)
	public ResponseMessage deleteViewDetailByAdmin(@RequestBody NotificationViewDetailResponseDto dto,@AuthenticationPrincipal Token token) {

		try {
			return notificationService.deleteViewDetailByAdmin(dto,token);
		} catch (Exception e) {
			ResponseMessage responseDto = new ResponseMessage();
			responseDto.setStatus(PMCConstant.STATUS_FAILURE);
			responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
			responseDto.setMessage("Failed to Delete View Detail.");
			return responseDto;
		}
	}
	
	@RequestMapping(value = "/deleteChannel", method = RequestMethod.POST)
	public ResponseMessage deleteChannel(@RequestBody NotificationViewSettingsDto dto,@AuthenticationPrincipal Token token) {

		return notificationService.deleteChannel(dto,token);
	}
}
