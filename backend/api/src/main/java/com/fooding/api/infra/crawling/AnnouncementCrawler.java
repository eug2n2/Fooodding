package com.fooding.api.infra.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.fooding.api.announcement.domain.Announcement;
import com.fooding.api.announcement.repository.AnnouncementRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnnouncementCrawler {

	private static final String BASE_URL = "https://www.koreafoodtruck.org";
	private final AnnouncementRepository announcementRepository;

	public void crawlAnnouncements() throws IOException {
		List<Announcement> announcementList = new ArrayList<>();

		Document doc = Jsoup.connect(BASE_URL + "/blank-6")
			.header("Accept-Language", "ko-KR,ko;q=0.9")
			.userAgent("Mozilla/5.0")
			.get();

		// 게시물 링크 크롤링
		Elements postLinks = doc.select(
			"a[href^=https://www.koreafoodtruck.org/blank-6/sa-hangugpudeuteureoghyeobhoe/]");

		for (Element link : postLinks) {
			String postLink = link.attr("href");

			boolean exists = announcementRepository.existsByUrl(postLink);
			if (exists) {
				continue;
			}

			Document postDoc = Jsoup.connect(postLink).get();
			String title = postDoc.select("h1").text();

			// 제목에서 날짜 제거
			title = title.replaceAll("\\[.*?\\]", "").trim();

			// 제목에 특정 문자열이 포함된 경우 전체 크롤링 건너뛰기
			if (title.contains("※ 푸드트럭 행사 한국푸드트럭협회 카카오톡 플러스친구 안내")) {
				continue;
			}

			// 상세 정보 크롤링 (일자, 운영시간, 장소)
			String eventDate = postDoc.select("p:contains(일자)").text().split(":")[1].trim();
			String operatingTime = postDoc.select("p:contains(운영시간)").text().split(":")[1].trim();
			String location = postDoc.select("p:contains(장소)").text().split(":")[1].trim();

			Announcement announcement = Announcement.builder()
				.url(postLink)
				.title(title)
				.date(eventDate)
				.time(operatingTime)
				.place(location)
				.build();

			announcementList.add(announcement);
		}

		if (!announcementList.isEmpty()) {
			announcementRepository.saveAll(announcementList);
		}
	}
}