package com.team33.modulequartz.subscription.controller;

import com.team33.modulecore.domain.item.mapper.ItemMapper;
import com.team33.modulecore.domain.order.dto.ItemOrderDto.SubResponse;
import com.team33.modulecore.domain.order.entity.ItemOrder;
import com.team33.modulecore.domain.order.entity.Order;
import com.team33.modulecore.domain.order.mapper.ItemOrderMapper;
import com.team33.modulecore.domain.order.service.ItemOrderService;
import com.team33.modulecore.domain.order.service.OrderService;
import com.team33.modulecore.global.response.SingleResponseDto;
import com.team33.modulequartz.subscription.service.SubscriptionService;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
@RestController
public class ScheduleController {

    private final SubscriptionService subscriptionService;
    private final OrderService orderService;
    private final ItemOrderService itemOrderService;
    private final ItemOrderMapper itemOrderMapper;
    private final ItemMapper itemMapper;

    private static final String SUCCESS_SHCHDULE = "스케쥴 구성 완료";


    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping
    public SingleResponseDto<String> schedule(@RequestParam(name = "orderId") Long orderId) {
        Order order = orderService.findOrder(orderId);
        List<ItemOrder> itemOrders = order.getItemOrders();

        applySchedule(orderId, order, itemOrders);

        return new SingleResponseDto(SUCCESS_SHCHDULE);
    }

    @PatchMapping("/change")
    public SingleResponseDto<SubResponse> changePeriod(
        @RequestParam(name = "orderId") Long orderId,
        @RequestParam(name = "period") Integer period,
        @RequestParam(name = "itemOrderId") Long itemOrderId
    ) {
        ItemOrder itemOrder = subscriptionService.changePeriod(orderId, period, itemOrderId);
        return new SingleResponseDto<>(
            itemOrderMapper.itemOrderToSubResponse(itemOrder, itemMapper)
        );
    }

    @PatchMapping("/delay")
    public SingleResponseDto<SubResponse> delay(
        @RequestParam(name = "orderId") Long orderId,
        @RequestParam(name = "delay") Integer delay,
        @RequestParam(name = "itemOrderId") Long itemOrderId
    ) {
        ItemOrder itemOrder = subscriptionService.delayDelivery(orderId, delay, itemOrderId);
        return new SingleResponseDto<>(
            itemOrderMapper.itemOrderToSubResponse(itemOrder, itemMapper));
    }

    @DeleteMapping("/cancel")
    public ZonedDateTime delete(
        @RequestParam(name = "orderId") Long orderId,
        @RequestParam(name = "itemOrderId") Long itemOrderId
    ) {
        subscriptionService.cancelScheduler(orderId, itemOrderId);
        return ZonedDateTime.now();
    }

    private void applySchedule(
        final Long orderId,
        final Order order,
        final List<ItemOrder> itemOrders
    ) {
        for (ItemOrder itemOrder : itemOrders) {
            ZonedDateTime nextDelivery = order.getCreatedAt().plusDays(itemOrder.getPeriod());
            ItemOrder itemOrder1 = itemOrderService.updateDeliveryInfo(
                orderId, order.getCreatedAt(), nextDelivery, itemOrder
            );
            subscriptionService.startSchedule(order, itemOrder1);
        }
    }
}



