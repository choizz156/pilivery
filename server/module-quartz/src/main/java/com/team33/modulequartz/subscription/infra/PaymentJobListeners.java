package com.team33.modulequartz.subscription.infra;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.team33.modulecore.exception.BusinessLogicException;
import com.team33.modulecore.exception.ExceptionCode;
import com.team33.modulequartz.subscription.domain.PaymentDateUpdatedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentJobListeners implements JobListener {

	private final ApplicationEventPublisher applicationEventPublisher;

	public static final Logger log = LoggerFactory.getLogger("fileLog");
	private static final String PAYMENT_JOB = "payment Job";
	private static final String RETRY = "retry";

	@Override
	public String getName() {
		return PAYMENT_JOB;
	}

	@Override
	public void jobToBeExecuted(final JobExecutionContext context) {
	}

	/**
	 * job 중단 시
	 *
	 * @param context
	 */
	@Override
	public void jobExecutionVetoed(final JobExecutionContext context) {
		JobKey key = context.getJobDetail().getKey();
		log.warn("중단된 job의 jobkey = {}", key);
	}

	/**
	 * job 실행 후 예외가 발생할 경우,
	 * - 첫 번째 예외 발생 시, 바로 job을 재실행한다.
	 * - 두 번째 예외 발생 시, 한 시간 후에 다시 재시도.
	 * 예외가 발생하지 않은 경우, 다음 job을 등록한다.
	 *
	 * @param context
	 * @param jobException
	 */
	@Override
	public void jobWasExecuted(
		final JobExecutionContext context,
		final JobExecutionException jobException
	) {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int retryCount = jobDataMap.getInt(RETRY);

		retryIfJobException(jobException, jobDataMap, retryCount);
		deleteJobIfJobExceptionMoreThan2(context.getJobDetail().getKey(), retryCount, jobException);

		long orderItemId = jobDataMap.getLong("orderItemId");
		applicationEventPublisher.publishEvent(new PaymentDateUpdatedEvent(orderItemId));
	}

	private void deleteJobIfJobExceptionMoreThan2(JobKey key, int retryCount, JobExecutionException jobException) {
		if (retryCount >= 2) {
			jobException.setUnscheduleAllTriggers(true);
			log.warn("job 예외로 인한 스케쥴 취소 = {}, 재시도 횟수 = {}, 메시지 = {}", key, retryCount, jobException.getMessage());
			throw new BusinessLogicException(ExceptionCode.SCHEDULE_CANCEL);
		}
	}

	private void retryIfJobException(
		final JobExecutionException jobException,
		final JobDataMap jobDataMap,
		final int retryCount
	) {
		if (jobException != null) {
			log.warn("job exception = {}, key = {}", jobException.getMessage(), jobDataMap.getKeys());
			retryImmediately(jobException, jobDataMap, retryCount);
		}
	}

	private void retryImmediately(
		final JobExecutionException jobException,
		final JobDataMap jobDataMap,
		int retryCount
	) {
		if (retryCount < 2) {
			log.warn("최초 재시도");
			jobException.setRefireImmediately(true);
			jobDataMap.put(RETRY, ++retryCount);
		}
	}
}
