package order;

import jdk.jfr.Description;
import user.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class OrderWithAuthorizedUserTest {
    private UserManagement userManagement;
    private OrderManagement orderManagement;
    private UserInformation userInformation;
    private Tokens token;
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
    @Description("Создание пользователя")
    public void setUp() {
        userManagement = new UserManagement();
        userInformation = UserCreator.getRandom();
        userManagement.createUser(userInformation);
        orderManagement = new OrderManagement();
        order = new BuilderOrders();
        token = userManagement.loginUser(LoginUser.from(userInformation))
                .extract().body()
                .as(Tokens.class);
    }
    @After
    @Description("Удаление пользователя")
    public void removeUser() {
        if (!(token.getAccessToken() == null)) {
            userManagement.removeUser(token.getAccessToken());
        }
    }
    @Test
    @Description("Создание заказа авторизованным пользователем")
    public void createOrderTest() {
        order.addIngredients(ingredient1);
        order.addIngredients(ingredient2);
        order.addIngredients(ingredient3);
        orderManagement.createOrder(token.getAccessToken(), order)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("order", notNullValue())
                .body("order.ingredients._id", hasItems(ingredient1, ingredient2, ingredient3))
                .body("order.status", equalTo("done"))
                .body("order.owner.name", equalTo(userInformation.getName()))
                .body("order.owner.email", equalTo(userInformation.getEmail().toLowerCase()));
    }
    @Test
    @Description("Получение информации заказа с авторизованным пользователем")
    public void getInfoOrderUserTest() {
        order.addIngredients(ingredient1);
        orderManagement.createOrder(token.getAccessToken(), order);
        orderManagement.getUserOrders(token.getAccessToken())
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue())
                .body("orders.status", hasItems("done"));
    }
    @Test
    @Description("Создание заказа с невалидным ингредиентом авторизованным пользователем")
    public void createOrderWithIncorrectIngredientByUserTest() {
        order.addIngredients(RandomStringUtils.randomAlphabetic(10));
        orderManagement.createOrder(token.getAccessToken(), order)
                .assertThat()
                .statusCode(500);
    }
    @Test
    @Description("Создание заказа без ингредиента с авторизованным пользователем")
    public void createOrderWithoutIngredientByUserTest() {
        orderManagement.createOrder(token.getAccessToken(), order)
                .assertThat()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}