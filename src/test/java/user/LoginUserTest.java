package user;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private UserInformation userInformation;
    private LoginUser loginUser;
    private UserManagement userManagement;
    private Tokens token;
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
    }
    @After
    @Description("Удаление пользователя")
    public void removeUser() {
        token = userManagement.loginUser(LoginUser.from(userInformation))
                .extract().body()
                .as(Tokens.class);
        if (!(token.getAccessToken() == null)) {
            userManagement.removeUser(token.getAccessToken());
        }
    }
    @Test
    @Description("Успешная авторизация пользователя")
    public void loginUserTest() {
        userManagement.loginUser(LoginUser.from(userInformation))
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email",equalTo(userInformation.getEmail().toLowerCase()))
                .body("user.name", equalTo(userInformation.getName()));
    }
    @Test
    @Description("Авторизация пользователя с неверным данными")
    public void loginWithIncorrectEmailAndPasswordTest() {
        loginUser = new LoginUser(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru",
                RandomStringUtils.randomAlphabetic(10));
        userManagement.loginUser(loginUser)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @Description("Авторизация пользователя с неверными данными")
    public void loginWithIncorrectEmailAndPasswordNumTwoTest() {
        loginUser = new LoginUser(userInformation.getEmail(),
                RandomStringUtils.randomAlphabetic(10));
        userManagement.loginUser(loginUser)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @Description("Авторизация пользователя с неверным Email")
    public void loginWithIncorrectEmailTest() {
        loginUser = new LoginUser(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", userInformation.getPassword());
        userManagement.loginUser(loginUser)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @Description("Авторизация пользователя под существующим Email")
    public void loginWithAlreadyExistEmailTest() {
        loginUser = new LoginUser(userInformation.getEmail(), RandomStringUtils.randomAlphabetic(10));
        userManagement.loginUser(loginUser)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @Description("Выход пользователя из личного кабинета")
    public void logoutUserTest() {
        token = userManagement.loginUser(LoginUser.from(userInformation))
                .extract().body()
                .as(Tokens.class);
        LogoutUser logout=new LogoutUser(token.getRefreshToken());
        userManagement.logoutUser(logout)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Successful logout"));
    }
}
