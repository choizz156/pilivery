package server.team33.scheduler.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.team33.order.entity.ItemOrder;
import server.team33.order.entity.Order;
import server.team33.order.service.OrderService;
import server.team33.scheduler.service.SchedulingService;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class SchedualingController {
    private final SchedulingService scheduler;
    private final OrderService orderService;

    @GetMapping
    public void startSchedule( @RequestParam(name = "orderId") Long orderId ) throws IOException{

        Order order = orderService.findOrder(orderId);
        List<ItemOrder> itemOrders = order.getItemOrders();

        for(ItemOrder itemOrder : itemOrders){
            scheduler.startScheduler(orderId, itemOrder);
        }
        log.info("스케쥴러 시작했음");
    }

    @GetMapping("/change")
    public void changeSchedule( @RequestParam(name = "orderId") Long orderId, @RequestParam(name = "period") Integer period ){

        log.warn("스케쥴 변경");
        Order order = orderService.findOrder(orderId);
        List<ItemOrder> itemOrders = order.getItemOrders();

        for(ItemOrder itemOrder : itemOrders){
            scheduler.changePeriod(orderId, itemOrder, period);
        }
    }

    @GetMapping("/cancel")
    public void cancelSchedule( @RequestParam(name = "orderId") Long orderId, @RequestParam(name = "itemOrderId") Long itemOrderId ){
        scheduler.stopScheduler(orderId, itemOrderId);
    }

    @PatchMapping("/delay")
    public void delayDelivery( @RequestParam(name = "orderId") Long orderId, @RequestParam(name = "itemOrderId") Long itemOrderId,
                               @RequestParam("delay") String delay ){

        Order order = orderService.findOrder(orderId);
        ItemOrder itemOrder = order.getItemOrders().get((int) (itemOrderId - 1));

        scheduler.delayDelivery(orderId, itemOrder, delay);
    }

}
