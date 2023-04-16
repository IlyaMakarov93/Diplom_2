package user;

import uri.URI;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserManagement extends URI {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserInformation user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }
    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(LoginUser userLogin) {
        return given()
                .spec(getBaseReqSpec())
                .body(userLogin)
                .when()
                .post(USER_LOGIN)
                .then();
    }
    @Step("Измемение всех данных авторизованного пользователя")
    public ValidatableResponse changeDataUser(String token, UserInformation user) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .and()
                .body(user)
                .when()
                .patch(USER_ACTIONS)
                .then();
    }
    @Step("Изменение всех данных неавторизованного пользователя")
    public ValidatableResponse changeDataUserWithoutToken(UserInformation user) {
        return given()
                .spec(getBaseReqSpec())
                .and()
                .body(user)
                .when()
                .patch(USER_ACTIONS)
                .then();
    }
    @Step("Выход авторизованного пользователя")
    public ValidatableResponse logoutUser(LogoutUser token) {
        return given()
                .spec(getBaseReqSpec())
                .body(token)
                .when()
                .post(USER_LOGOUT)
                .then();
    }
    @Step("Удаление авторизованного пользователя")
    public void removeUser(String token) {
        given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .when()
                .delete(USER_ACTIONS)
                .then();
    }
}


