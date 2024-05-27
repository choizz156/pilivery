// package com.team33.modulecore.order.application;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.BDDMockito.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.springframework.context.ApplicationContext;
//
// import com.team33.modulecore.FixtureMonkeyFactory;
// import com.team33.modulecore.common.OrderFindHelper;
// import com.team33.modulecore.common.UserFindHelper;
// import com.team33.modulecore.item.domain.entity.Item;
// import com.team33.modulecore.order.domain.Address;
// import com.team33.modulecore.order.domain.OrderItem;
// import com.team33.modulecore.order.domain.OrderPrice;
// import com.team33.modulecore.order.domain.OrderStatus;
// import com.team33.modulecore.order.domain.Receiver;
// import com.team33.modulecore.order.domain.entity.Order;
// import com.team33.modulecore.order.domain.repository.OrderRepository;
// import com.team33.modulecore.order.dto.OrderContext;
// import com.team33.modulecore.order.events.CartRefreshedEvent;
// import com.team33.modulecore.order.events.ItemSaleCountedEvent;
// import com.team33.modulecore.order.mock.FakeOrderRepository;
// import com.team33.modulecore.user.domain.entity.User;
//
// @TestInstance(Lifecycle.PER_CLASS)
// class OrderServiceTest {
//
// 	private User user;
// 	private OrderRepository orderRepository;
//
// 	@BeforeAll
// 	void beforeAll() {
// 		getMockItem();
// 		user = getMockUser();
// 		orderRepository = new FakeOrderRepository();
// 	}
//
// 	@DisplayName("order 객체를 생성할 수 있다.")
// 	@Test
// 	void callOrderTest() throws Exception {
// 		//given
// 		OrderContext orderContext = OrderContext.builder()
// 			.isSubscription(false)
// 			.isOrderedCart(true)
// 			.userId(1L)
// 			.receiver(Receiver.builder()
// 				.realName("홍홍홍")
// 				.phone("010-1234-5678")
// 				.address(new Address("인천 부평구", "한국아파트 101"))
// 				.build()
// 			)
// 			.build();
//
// 		var orderItems = getMockOrderItems();
// 		var userFindHelper = mock(UserFindHelper.class);
// 		given(userFindHelper.findUser(anyLong())).willReturn(user);
//
// 		var orderService =
// 			new OrderCreateService(orderRepository, userFindHelper);
//
// 		//when
// 		Order order = orderService.callOrder(orderItems, orderContext);
//
// 		//then
// 		assertThat(order.getOrderPrice()).isEqualTo(new OrderPrice(orderItems));
// 		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.REQUEST);
// 		assertThat(order.getOrderItems()).hasSize(3);
// 		assertThat(order.getTotalItemsCount()).isEqualTo(3);
// 		assertThat(order.getUser()).isEqualTo(user);
// 	}
//
// 	@DisplayName("일반 주문의 주문 상태를 구독 중으로 바꿀 수 있다.")
// 	@Test
// 	void 주문_상태_변경1() throws Exception {
// 		//given
// 		var order = orderRepository.save(getNoCartOrder());
//
// 		ApplicationContext applicationContext = mock(ApplicationContext.class);
// 		OrderFindHelper orderFindHelper = mock(OrderFindHelper.class);
//
// 		when(orderFindHelper.findOrder(anyLong())).thenReturn(order);
//
// 		var orderService =
// 			new OrderStatusService(applicationContext, new OrderFindHelper(orderRepository),
// 				new OrderPaymentCodeService(orderFindHelper));
//
// 		//when
// 		orderService.processSubscriptionStatus(order.getId(), "sid");
//
// 		//then
// 		verify(applicationContext, times(1)).publishEvent(any(CartRefreshedEvent.class));
// 		verify(applicationContext, times(1)).publishEvent(any(ItemSaleCountedEvent.class));
//
// 		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.SUBSCRIBE);
// 	}
//
// 	@DisplayName("카트 주문의 주문 상태를 구독 중으로 바꿀 수 있다.")
// 	@Test
// 	void 주문_상태_변경2() throws Exception {
// 		//given
// 		var order = orderRepository.save(getCartOrder());
// 		ApplicationContext applicationContext = mock(ApplicationContext.class);
// 		var orderService =
// 			new OrderStatusService(applicationContext, new OrderFindHelper(orderRepository),
// 				new OrderPaymentCodeService(new OrderFindHelper(orderRepository)));
//
// 		//when
// 		orderService.processSubscriptionStatus(order.getId(), "sid");
//
// 		//then
// 		verify(applicationContext, times(1)).publishEvent(any(CartRefreshedEvent.class));
// 		verify(applicationContext, times(1)).publishEvent(any(ItemSaleCountedEvent.class));
//
// 		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.SUBSCRIBE);
// 	}
//
// 	@DisplayName("일반 주문 상태를 주문 완료로 바꿀 수 있다.")
// 	@Test
// 	void 주문_상태_변경4() throws Exception {
// 		//given
// 		Order sample = FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("isOrderedAtCart", false)
// 			.set("isSubscription", true)
// 			.setNull("receiver")
// 			.setNull("orderItems")
// 			.sample();
//
// 		var order = orderRepository.save(sample);
//
// 		ApplicationContext applicationContext = mock(ApplicationContext.class);
//
// 		var orderService =
// 			new OrderStatusService(applicationContext, new OrderFindHelper(orderRepository), null);
//
// 		//when
// 		orderService.processOneTimeStatus(order.getId());
//
// 		//then
// 		verify(applicationContext, times(1)).publishEvent(any(CartRefreshedEvent.class));
// 		verify(applicationContext, times(1)).publishEvent(any(ItemSaleCountedEvent.class));
//
// 		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.COMPLETE);
// 	}
//
// 	@DisplayName("카트 주문 상태를 주문 완료로 바꿀 수 있다.")
// 	@Test
// 	void 주문_상태_변경5() throws Exception {
// 		//given
// 		Order sample = FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("isOrderedAtCart", true)
// 			.set("isSubscription", false)
// 			.setNull("receiver")
// 			.setNull("orderItems")
// 			.sample();
//
// 		var order = orderRepository.save(sample);
// 		ApplicationContext applicationContext = mock(ApplicationContext.class);
//
// 		var orderService =
// 			new OrderStatusService(applicationContext, new OrderFindHelper(orderRepository), null);
//
// 		//when
// 		orderService.processOneTimeStatus(order.getId());
//
// 		//then
// 		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.COMPLETE);
//
// 	}
//
// 	@DisplayName("주문 상태를 취소로 바꿀 수 있다.")
// 	@Test
// 	void 주문_상태_변경6() throws Exception {
// 		//given
// 		Order sample = FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("isOrderedAtCart", true)
// 			.set("isSubscription", false)
// 			.setNull("receiver")
// 			.setNull("orderItems")
// 			.sample();
//
// 		var order = orderRepository.save(sample);
// 		ApplicationContext applicationContext = mock(ApplicationContext.class);
//
// 		var orderService =
// 			new OrderStatusService(applicationContext, new OrderFindHelper(orderRepository), null);
//
// 		//when
// 		orderService.processCancel(order.getId(), refundContext);
//
// 		//then
// 		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.Refund);
// 	}
//
// 	@DisplayName("order 객체에 tid를 저장할 수 있다.")
// 	@Test
// 	void 주문_상태_변경7() throws Exception {
// 		//given
// 		Order sample = FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("isOrderedAtCart", true)
// 			.set("isSubscription", false)
// 			.setNull("receiver")
// 			.setNull("orderItems")
// 			.sample();
//
// 		var order = orderRepository.save(sample);
//
// 		var orderService =
// 			new OrderPaymentCodeService(new OrderFindHelper(orderRepository));
//
// 		//when
// 		orderService.addTid(order.getId(), "tid");
//
// 		//then
// 		assertThat(order.getPaymentCode().getTid()).isNotNull();
// 	}
//
// 	@DisplayName("정기 결제 중인 아이템의 수량을 조정할 수 있다.")
// 	@Test
// 	void 정기_구독_수량_조정() throws Exception {
// 		//given
// 		var order = orderRepository.save(getMockOrderWithOrderItem());
//
// 		var orderService =
// 			new OrderSubscriptionService(new OrderFindHelper(orderRepository));
//
// 		//when
// 		orderService.changeSubscriptionItemQuantity(
// 			order.getId(),
// 			order.getOrderItems().get(0).getId(),
// 			2
// 		);
//
// 		//then
// 		assertThat(order.getOrderItems().get(0).getQuantity()).isEqualTo(2);
// 		assertThat(order.getTotalQuantity()).isEqualTo(8);
// 		assertThat(order.getOrderPrice().getTotalPrice()).isEqualTo(8000);
// 		assertThat(order.getOrderPrice().getTotalDiscountPrice()).isEqualTo(4000);
// 	}
//
// 	@Test
// 	void 주문_복사() throws Exception {
// 		//given
// 		var orderService =
// 			new OrderCreateService(orderRepository, null);
// 		Order order = getMockOrderWithOrderItem();
//
// 		//when
// 		Order copyOrder = orderService.deepCopy(order);
//
// 		//then
// 		assertThat(copyOrder).usingRecursiveComparison()
// 			.ignoringFields("id", "createdAt", "updatedAt")
// 			.isEqualTo(order);
// 	}
//
// 	private Item getMockItem() {
// 		return FixtureMonkeyFactory.get().giveMeBuilder(Item.class)
// 			.set("id", 1L)
// 			.set("statistics.sales", 1)
// 			.set("information.price.discountRate", 3D)
// 			.set("information.price.realPrice", 3000)
// 			.set("information.productName", "mockItem")
// 			.sample();
// 	}
//
// 	private User getMockUser() {
// 		return FixtureMonkeyFactory.get().giveMeBuilder(User.class)
// 			.set("id", null)
// 			.set("cartId", null)
// 			.sample();
// 	}
//
// 	private List<OrderItem> getMockOrderItems() {
// 		return FixtureMonkeyFactory.get().giveMeBuilder(OrderItem.class)
// 			.set("item", getMockItem())
// 			.set("quantity", 3)
// 			.set("item.information.price.realPrice", 1000)
// 			.set("item.information.price.discountPrice", 500)
// 			.sampleList(3);
// 	}
//
// 	private Order getNoCartOrder() {
// 		return FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("isOrderedAtCart", false)
// 			.set("isSubscription", false)
// 			.setNull("paymentCode.sid")
// 			.sample();
// 	}
//
// 	private Order getCartOrder() {
// 		return FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("isOrderedAtCart", true)
// 			.set("isSubscription", true)
// 			.setNull("paymentCode.sid")
// 			.sample();
// 	}
//
// 	private Order getMockOrderWithOrderItem() {
// 		return FixtureMonkeyFactory.get().giveMeBuilder(Order.class)
// 			.set("id", 1L)
// 			.set("orderItems", getMockOrderItems())
// 			.set("totalQuantity", 9)
// 			.set("orderPrice", new OrderPrice(getMockOrderItems()))
// 			.set("user", user)
// 			.set("paymentCode.sid", "sid")
// 			.sample();
// 	}
// }