package com.fooding.api.announcement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooding.api.announcement.controller.response.GetAnnouncementRes;
import com.fooding.api.announcement.service.AnnouncementCommandService;
import com.fooding.api.announcement.service.AnnouncementQueryService;
import com.fooding.api.announcement.service.dto.AnnouncementDto;
import com.fooding.api.announcement.service.dto.AnnouncementLogDto;
import com.fooding.api.core.aop.annotation.RequireJwtToken;
import com.fooding.api.core.aop.member.MemberContext;
import com.fooding.api.core.template.response.BaseResponse;
import com.fooding.api.fcm.domain.TokenStatus;
import com.fooding.api.fcm.service.FcmTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/announcement")
@RestController
public class AnnouncementController {

	private final AnnouncementQueryService announcementQueryService;
	private final AnnouncementCommandService announcementCommandService;
	private final FcmTokenService fcmTokenService;

	@RequireJwtToken
	@PostMapping("/{announcement_id}/open")
	public ResponseEntity<BaseResponse<?>> openAnnouncement(
		@PathVariable("announcement_id") Long announcementId) {
		announcementQueryService.saveAnnouncementLog(
			AnnouncementLogDto.builder()
				.memberId(MemberContext.getMemberId())
				.announcementId(announcementId)
				.build()
		);
		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}

	@RequireJwtToken
	@GetMapping("")
	public ResponseEntity<BaseResponse<GetAnnouncementRes>> getAnnouncements() {
		Long ownerId = MemberContext.getMemberId();
		List<AnnouncementDto> announcements = announcementCommandService.getAnnouncementByOwner(ownerId);
		TokenStatus tokenStatus = fcmTokenService.getTokenStatus(ownerId);
		GetAnnouncementRes response = GetAnnouncementRes.builder()
			.announcements(announcements)
			.tokenStatus(tokenStatus)
			.build();
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

}
