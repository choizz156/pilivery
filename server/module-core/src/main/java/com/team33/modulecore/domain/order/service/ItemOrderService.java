package com.team33.modulecore.domain.order.service;


import com.team33.modulecore.domain.item.repository.ItemRepository;
import com.team33.modulecore.domain.order.entity.ItemOrder;
import com.team33.modulecore.domain.order.entity.Order;
import com.team33.modulecore.domain.order.reposiroty.ItemOrderRepository;
import com.team33.modulecore.domain.order.reposiroty.OrderRepository;
import com.team33.modulecore.global.exception.BusinessLogicException;
import com.team33.modulecore.global.exception.ExceptionCode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemOrderService {

    private final ItemOrderRepository itemOrderRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;


    public List<ItemOrder> createItemOrder(ItemOrder itemOrder) {
        itemOrderRepository.save(itemOrder);
        List<ItemOrder> itemOrders = new ArrayList<>();
        itemOrders.add(itemOrder);

        return itemOrders;
    }

    public ItemOrder findItemOrder(long itemOrderId) {
        Optional<ItemOrder> optionalItemOrder = itemOrderRepository.findById(itemOrderId);
        ItemOrder itemOrder = optionalItemOrder.orElseThrow(() -> new BusinessLogicException(
            ExceptionCode.ORDER_NOT_FOUND));

        return itemOrder;
    }

    public ItemOrder changeSubQuantity(long itemOrderId, int upDown) {
        ItemOrder itemOrder = findItemOrder(itemOrderId);

        itemOrder.setQuantity(itemOrder.getQuantity() + upDown);
        itemOrderRepository.save(itemOrder);

        return itemOrder;
    }

    public int countTotalPrice(List<ItemOrder> itemOrders) {

        if (itemOrders == null) {
            return 0;
        }

        int totalPrice = 0;

        for (ItemOrder itemOrder : itemOrders) {
            int quantity = itemOrder.getQuantity();
            int price = itemOrder.getItem().getPrice();
            totalPrice += (quantity * price);
        }

        return totalPrice;
    }

    public int countDiscountTotalPrice(List<ItemOrder> itemOrders) {

        if (itemOrders == null) {
            return 0;
        }

        int totalDiscountPrice = 0;

        for (ItemOrder itemOrder : itemOrders) {
            int quantity = itemOrder.getQuantity();
            int price = itemOrder.getItem().getPrice();
            int discountRate = itemOrder.getItem().getDiscountRate();
            totalDiscountPrice += (quantity * price * discountRate / 100);
        }

        return totalDiscountPrice;
    }

    public int countQuantity(List<ItemOrder> itemOrders) { // 주문의 담긴 상품의 총량을 구하는 메서드

        if (itemOrders == null) {
            return 0;
        }

        int totalquantity = 0;

        for (ItemOrder itemOrder : itemOrders) {
            int quantity = itemOrder.getQuantity();
            totalquantity += quantity;
        }

        return totalquantity;
    }

    public void minusSales(List<ItemOrder> itemOrders) { // 주문 취소할 경우 아이템 판매량에서 제외

        for (ItemOrder itemOrder : itemOrders) {
            int sales = itemOrder.getQuantity();
            itemOrder.getItem().setSales(itemOrder.getItem().getSales() - sales);
            itemRepository.save(itemOrder.getItem());
        }
    }

    public void plusSales(ItemOrder itemOrder) { // 주문 요청할 경우 아이템 판매량 증가

        int sales = itemOrder.getQuantity();
        itemOrder.getItem().setSales(itemOrder.getItem().getSales() + sales);
        itemRepository.save(itemOrder.getItem());
    }

    public void setItemPeriod(Integer period, ItemOrder itemOrder) {
        itemOrder.setPeriod(period);
        log.error("주기변경 = {}", itemOrder.getPeriod());
    }

    public ItemOrder delayDelivery(Long orderId, Integer delay, ItemOrder io) {

        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            ItemOrder itemOrder = getItemOrder(io, order);
            ZonedDateTime nextDelivery = itemOrder.getNextDelivery().plusDays(delay);
            itemOrder.setNextDelivery(nextDelivery);
            return itemOrder;
        }
        throw new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND);
    }


    public ItemOrder updateDeliveryInfo(
        ZonedDateTime paymentDay,
        ZonedDateTime nextDelivery,
        ItemOrder itemOrder
    ) {
        itemOrder.setPaymentDay(paymentDay);
        itemOrder.setNextDelivery(nextDelivery);
        return itemOrder;
    }

    public ItemOrder itemOrderCopy(Long lastOrderId, Order newOrder, ItemOrder io) {
        Optional<Order> orderEntity = orderRepository.findById(lastOrderId);

        if (orderEntity.isPresent()) {
            ItemOrder itemOrder = new ItemOrder(getItemOrder(io, orderEntity));
            itemOrder.setOrder(newOrder);
            plusSales(itemOrder);
            itemOrderRepository.save(itemOrder);
            return itemOrder;
        }
        throw new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND);
    }

    public void cancelItemOrder(Long orderId, ItemOrder itemOrder) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            ItemOrder itemOrderInOrder = getItemOrder(itemOrder, order);
            itemOrderInOrder.setSubscription(false);
            log.warn("is subsucription = {}", itemOrderInOrder.isSubscription());
        }

    }

    private ItemOrder getItemOrder(ItemOrder io, Optional<Order> order) {
        int i = order.get().getItemOrders().indexOf(io);
        return order.get().getItemOrders().get(i);
    }
}
