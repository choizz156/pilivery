package com.server.modulequartz.subscription.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.team33.ModuleApiApplication;
import com.team33.modulecore.domain.item.entity.Item;
import com.team33.modulecore.domain.order.entity.ItemOrder;
import com.team33.modulecore.domain.order.entity.Order;
import com.team33.modulecore.domain.order.reposiroty.OrderRepository;
import com.team33.modulecore.domain.order.service.OrderService;
import com.team33.modulecore.domain.user.entity.User;
import com.team33.ModuleQuartzApplication;
import com.team33.modulequartz.config.AutoWiringSpringBeanJobFactory;
import com.team33.modulequartz.config.QuartzConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.Filter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import com.team33.ApiTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.ResponseStatus;

//TODO: 정확하게 알아보기위해서... 지워야한다.
@ActiveProfiles("quartz")
@Import(ModuleQuartzApplication.class)
class ScheduleControllerTest extends ApiTest {

    @MockBean(name = "orderService")
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.create();

    @DisplayName("요청 시 스케쥴러가 설정된다.")
    @RepeatedTest(10)
    void test1() throws Exception {
        //given
        User user = fixtureMonkey.giveMeBuilder(User.class)
            .set("userId", 1L)
            .sample();

        Item item = fixtureMonkey.giveMeBuilder(Item.class).set("title", "testItem").sample();

        ItemOrder itemOrder = fixtureMonkey.giveMeBuilder(ItemOrder.class)
            .set("period", 30)
            .set("item", item)
            .sample();

        Order order = fixtureMonkey.giveMeBuilder(Order.class)
            .set("user", user)
            .set("orderId", 1L)
            .set("itemOrders", List.of(itemOrder))
            .set("createdAt", ZonedDateTime.now())
            .sample();

        BDDMockito.given(orderService.findOrder(anyLong()))
            .willReturn(order);
        BDDMockito.given(orderRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(order));

        Long orderId = order.getOrderId();

        //when
        //@formatter:off
        ExtractableResponse<Response> response =
            given()
                .log().all()
                .param("orderId", orderId)
            .when()
                .get("/schedule")
            .then()
                .log().all().extract();
        //@formatter:on

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
        assertThat(response.body().jsonPath().get("data").toString()).hasToString("스케쥴 구성 완료");
    }
}