package com.team33.moduleexternalapi.infra.webflux;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team33.moduleexternalapi.exception.PaymentApiException;
import com.team33.moduleexternalapi.infra.KakaoHeader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebFluxClient {

	private final ObjectMapper objectMapper;
	private final WebClient webClient;

	public <T> CompletableFuture<T> send(Map<String, Object> params, String uri, Class<T> responseClass) throws JsonProcessingException {

		String kakaoRequest = objectMapper.writeValueAsString(params);

		Mono<T> mono = webClient.post()
			.uri(uri)
			.headers(httpHeaders -> httpHeaders.addAll(KakaoHeader.HTTP_HEADERS.getHeaders()))
			.bodyValue(kakaoRequest)
			.retrieve()
			.onStatus(httpStatus -> !httpStatus.is2xxSuccessful(), clientResponse -> clientResponse.bodyToMono(String.class)
				.flatMap(errorBody -> Mono.error(new PaymentApiException("HTTP Status " + clientResponse.statusCode() + ": " + errorBody))))
			.bodyToMono(responseClass);

		return mono.toFuture();
	}

}
