package com.fooding.api.announcement.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fooding.api.announcement.domain.Announcement;
import com.fooding.api.announcement.domain.AnnouncementLog;
import com.fooding.api.announcement.exception.NoAnnouncementException;
import com.fooding.api.announcement.repository.AnnouncementLogRepository;
import com.fooding.api.announcement.repository.AnnouncementRepository;
import com.fooding.api.announcement.repository.custom.AnnouncementLogRepositoryCustom;
import com.fooding.api.announcement.service.AnnouncementQueryService;
import com.fooding.api.announcement.service.dto.AnnouncementLogDto;
import com.fooding.api.member.domain.Member;
import com.fooding.api.member.exception.NoMemberException;
import com.fooding.api.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
class AnnouncementQueryServiceImpl implements AnnouncementQueryService {

	private final MemberRepository memberRepository;
	private final AnnouncementRepository announcementRepository;
	private final AnnouncementLogRepository announcementLogRepository;
	private final AnnouncementLogRepositoryCustom announcementLogRepositoryCustom;

	@Override
	public void saveAnnouncementLog(AnnouncementLogDto announcementLogDto) {
		Member member = memberRepository.findById(announcementLogDto.memberId())
			.orElseThrow(() -> new NoMemberException("Owner not found with ID:" + announcementLogDto.memberId()));

		Announcement announcement = announcementRepository.findById(announcementLogDto.announcementId())
			.orElseThrow(() -> new NoAnnouncementException(
				"Announcement not found with ID: " + announcementLogDto.announcementId()));

		boolean logExists = announcementLogRepositoryCustom.logExists(member, announcement);

		if (!logExists) {  // 관련 로그 존재하지 않으면 저장
			AnnouncementLog log = AnnouncementLog.builder()
				.member(member)
				.announcement(announcement)
				.build();

			announcementLogRepository.save(log);
		}
	}
}
