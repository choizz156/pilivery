package com.team33.modulecore.payment.kakao.application.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleRegisteredEvent {
	private final long params;
}
