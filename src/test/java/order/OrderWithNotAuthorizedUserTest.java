package order;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class OrderWithNotAuthorizedUserTest {
    private OrderManagement orderManagement;
    private BuilderOrders order;
    private final String ingredient1 = "61c0c5a71d1f82001bdaaa7a";
    private final String ingredient2 = "61c0c5a71d1f82001bdaaa79";
    private final String ingredient3 = "61c0c5a71d1f82001bdaaa73";

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }
    @Before
    public void setUp() {
        orderManagement = new OrderManagement();
        order = new BuilderOrders();
    }
    @Test
    @Description("Создание заказа неавторизованным пользователем")
    public void createOrderTest() {
        order.addIngredients(ingredient1);
        order.addIngredients(ingredient2);
        order.addIngredients(ingredient3);
        orderManagement.createOrderWithoutToken(order)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("order", notNullValue())
                .body("order.number", notNullValue());
    }
    @Test
    @Description("Получение данных заказа неавторизованным пользователем")
    public void getUserOrdersWithoutTokenTest() {
        order.addIngredients(ingredient1);
        orderManagement.getUserOrdersWithoutToken()
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
   }
    @Test
    @Description("Создание заказа с невалидным ингредиентом неавторизованным пользователем")
    public void createOrderWithIncorrectIngredientTest() {
        order.addIngredients(RandomStringUtils.randomAlphabetic(10));
        orderManagement.createOrderWithoutToken(order)
                .assertThat()
                .statusCode(500);
    }
    @Test
    @Description("Создание заказа без ингредиента неавторизованным пользователем")
    public void createOrderWithoutIngredientTest() {
        orderManagement.createOrderWithoutToken(order)
                .assertThat()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}
