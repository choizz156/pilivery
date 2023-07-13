package com.modulequartz.quartz;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.team33.ApiTest;
import com.team33.ModuleQuartzApplication;
import com.team33.UserAccount;
import com.team33.modulecore.domain.item.entity.Item;
import com.team33.modulecore.domain.order.entity.ItemOrder;
import com.team33.modulecore.domain.order.entity.Order;
import com.team33.modulecore.domain.order.reposiroty.OrderRepository;
import com.team33.modulecore.domain.order.service.ItemOrderService;
import com.team33.modulecore.domain.order.service.OrderService;
import com.team33.modulecore.domain.user.entity.User;
import com.team33.modulecore.domain.user.service.UserService;
import com.team33.modulecore.global.security.jwt.JwtTokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("quartz")
@Import(ModuleQuartzApplication.class)
class ScheduleControllerTest extends ApiTest {

    @MockBean(name = "orderService")
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ItemOrderService itemOrderService;

    private User user;
    private Order order;
    private ItemOrder itemOrder;
    private Item item;
    private ZonedDateTime now;


    private final FixtureMonkey fixtureMonkey = FixtureMonkey
        .builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    @BeforeEach
    void setUp() {
        user = fixtureMonkey.giveMeBuilder(User.class)
            .set("userId", 1L)
            .sample();

        item = fixtureMonkey.giveMeBuilder(Item.class)
            .set("itemId", 1L)
            .set("title", "testItem").sample();

        now = ZonedDateTime.now();

        itemOrder = fixtureMonkey.giveMeBuilder(ItemOrder.class)
            .set("itemOrderId", 1L)
            .set("period", 30)
            .set("paymentDay", now)
            .set("nextDelivery", now.plusDays(30))
            .set("quantity", 1)
            .set("item", item)
            .sample();

        order = fixtureMonkey.giveMeBuilder(Order.class)
            .set("user", user)
            .set("orderId", 1L)
            .set("createdAt", now)
            .sample();

        order.setItemOrders(List.of(itemOrder));
        itemOrder.setOrder(order);

        given(orderService.findOrder(anyLong()))
            .willReturn(order);
        given(orderRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(order));
        given(itemOrderService.findItemOrder(anyLong()))
            .willReturn(itemOrder);

        //다음 배송일 업데이트
        itemOrder.setNextDelivery(itemOrder.getPaymentDay().plusDays(60));
        itemOrder.setPaymentDay(itemOrder.getPaymentDay().plusDays(60));

        given(itemOrderService.updateDeliveryInfo(any(), any(), any(ItemOrder.class)))
            .willReturn(itemOrder);
    }

    @DisplayName("요청 시 스케쥴러가 설정된다.")
    @Test
    void test1() throws Exception {

        Long orderId = order.getOrderId();
        //@formatter:off
        given()
                .log().all()
                .param("orderId", orderId)
        .when()
                .get("/schedule")
        .then()
                .assertThat().statusCode(HttpStatus.ACCEPTED.value())
                .assertThat().body(containsString("스케쥴 구성 완료"))
                .log().all();
        //@formatter:on

    }

    @DisplayName("스케쥴을 수정할 수 있다.")
    @UserAccount({"test","010-0000-0000"})
    @Test
    void test2() throws Exception {

        String token = super.getToken();

        ExtractableResponse<Response> response =
            //@formatter:off
            given()
                    .log().all()
                    .header("Authorization", token)
                    .param("period", 60)
                    .param("orderId", 1L)
                    .param("itemOrderId", 1L)
            .when()
                    .patch("/schedule")
            .then()
                    .log().all()
                    .statusCode(HttpStatus.ACCEPTED.value())
                    .extract();
            //@formatter:on

        String year = response.jsonPath().get("data.nextDelivery").toString().substring(0, 4);
        String month = response.jsonPath().get("data.nextDelivery").toString().substring(6, 7);
        String day = response.jsonPath().get("data.nextDelivery").toString().substring(8, 10);

        assertThat(year).isEqualTo(String.valueOf(itemOrder.getPaymentDay().getYear()));
        assertThat(month).isEqualTo(
            String.valueOf(itemOrder.getPaymentDay().getMonth().getValue())
        );
        assertThat(day).isEqualTo(
            String.valueOf(itemOrder.getPaymentDay().getDayOfMonth())
        );
    }

    @DisplayName("스케쥴을 취소할 수 있다.")
    @UserAccount({"test", "010-0000-0000"})
    @Test
    void test3() throws Exception {

        String token = super.getToken();

        ExtractableResponse<Response> response =
            //@formatter:off
            given()
                    .log().all()
                    .header("Authorization", token)
                    .param("orderId", 1L)
                    .param("itemOrderId", 1L)
            .when()
                    .delete("/schedule")
            .then()
                    .log().all()
                    .statusCode(HttpStatus.ACCEPTED.value())
                    .extract();
            //@formatter:on

        String year = response.jsonPath().get("data").toString().substring(0, 4);
        String month = response.jsonPath().get("data").toString().substring(6, 7);
        String day = response.jsonPath().get("data").toString().substring(8, 10);

        assertThat(year).isEqualTo(String.valueOf(ZonedDateTime.now().getYear()));
        assertThat(month).isEqualTo(String.valueOf(ZonedDateTime.now().getMonth().getValue()));
        assertThat(day).isEqualTo(String.valueOf(ZonedDateTime.now().getDayOfMonth()));
    }
}