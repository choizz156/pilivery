package com.team33.modulecore.payment.kakao.application;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.team33.modulecore.order.domain.entity.Order;
import com.team33.modulecore.payment.application.NormalApprove;
import com.team33.modulecore.payment.dto.ApproveRequest;
import com.team33.modulecore.payment.kakao.dto.KaKaoApproveResponse;
import com.team33.modulecore.payment.kakao.dto.KakaoApproveOneTimeRequest;
import com.team33.modulecore.payment.kakao.infra.ParameterProvider;

@Service
public class NormalApproveService
	extends KaKaoTemplate
	implements NormalApprove<KaKaoApproveResponse, KakaoApproveOneTimeRequest>
{
	private final ParameterProvider parameterProvider;
	// private final RestTemplate restTemplate;
	// private static final String KAKAO_APPROVE_URL = "https://kapi.kakao.com/v1/payment/approve";

	public NormalApproveService(RestTemplate restTemplate, ParameterProvider parameterProvider) {
		super(restTemplate);
		this.parameterProvider = parameterProvider;
	}

	@Override
	public KaKaoApproveResponse approveOneTime(KakaoApproveOneTimeRequest approveRequest) {
		// var oneTimeApproveParams = parameterProvider.getOneTimeApproveParams(
		// 	approveRequest.getTid(),
		// 	approveRequest.getPgtoken(),
		// 	approveRequest.getOrderId()
		// );
		//
		// return getResponseDtoAboutApprove(oneTimeApproveParams);
		return super.approve(approveRequest);
	}

	// private KaKaoApproveResponse getResponseDtoAboutApprove(
	// 	MultiValueMap<String, String> params
	// ) {
	// 	var entity = new HttpEntity<>(params, super.getHeaders());
	//
	// 	return restTemplate.postForObject(
	// 		KAKAO_APPROVE_URL,
	// 		entity,
	// 		KaKaoApproveResponse.class
	// 	);
	// }

	@Override
	public MultiValueMap<String, String> getRequestParams(Order order) {
		return null;
	}


	@Override
	public MultiValueMap<String, String> getApproveParams(ApproveRequest approveRequest) {
		KakaoApproveOneTimeRequest request = (KakaoApproveOneTimeRequest) approveRequest;

		return parameterProvider.getOneTimeApproveParams(
			request.getTid(),
			request.getPgtoken(),
			request.getOrderId()
		);
	}
}
