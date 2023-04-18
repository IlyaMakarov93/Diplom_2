package order;

import uri.URI;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderManagement extends URI {
    @Step("Cоздание заказа авторизованным пользователем")
    public ValidatableResponse createOrder(String token, BuilderOrders order) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .body(order)
                .when()
                .post(USER_ORDER)
                .then();
    }
    @Step("Cоздание заказа неавторизованным пользователем")
    public ValidatableResponse createOrderWithoutToken(BuilderOrders order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(USER_ORDER)
                .then();
    }
    @Step("Получение данных заказа авторизованным пользователем")
    public ValidatableResponse getUserOrders(String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .when()
                .get(USER_ORDER)
                .then();
    }
    @Step("Получение данных заказа неавторизованным пользователем")
    public ValidatableResponse getUserOrdersWithoutToken() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(USER_ORDER)
                .then();
    }

}
